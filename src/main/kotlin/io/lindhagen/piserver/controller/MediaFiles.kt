package io.lindhagen.piserver.controller

import io.lindhagen.piserver.exception.BadRequestException
import io.lindhagen.piserver.exception.NotFoundException
import io.lindhagen.piserver.model.Episode
import io.lindhagen.piserver.model.Media
import io.lindhagen.piserver.repository.EpisodeRepository
import io.lindhagen.piserver.repository.MediaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    22.10.2017
 */
@RestController
@RequestMapping("/api/v1/files")
class MediaFiles {

    val RESOURCE_FOLDER = "resources/media";
    val VALID_TYPES = mapOf("video/mp4" to "mp4")

    @Autowired
    lateinit var mediaRepo: MediaRepository

    @Autowired
    lateinit var episodeRepo: EpisodeRepository

    @PostMapping("")
    fun uploadVideo(@RequestParam(name = "id", required = true) id: Long,
                    @RequestParam(name = "episodeId", required = false) episodeId: Long?,
                    @RequestBody file: MultipartFile): Media {

        var media = mediaRepo.findOne(id) ?: throw NotFoundException("Cannot find media with id: $id")


        if (!media.isTvShow() && episodeId != null) {
            throw BadRequestException("Cannot add episode to a non `tv-show` media: ${media.id}")
        } else if(media.isTvShow() && episodeId === null) {
            throw BadRequestException("Cannot upload video to tv-show, without an `episodeId`")
        }

        // TODO(Find better null handler, kotlin has many)
        var episode: Episode? = null

        if (episodeId != null) {
            episode = episodeRepo.findOne(episodeId) ?:
                    throw NotFoundException("Cannot find episode with id: $episodeId")
        }

        val urls = uploadFiles(listOf(file))

        // Update url on media if episode is not null
        if (episode == null) {
            media.url = urls[0]
            return mediaRepo.save(media)
        } else {
            episode.url = urls[0]
            episodeRepo.save(episode)
            return mediaRepo.findOne(id) // Get the latest instance of media (with latest episode values)
        }
    }


    private fun uploadFiles(files: List<MultipartFile>): MutableList<String> {
        val urls = mutableListOf<String>()

        files.forEach({
            if (!it.isEmpty) {

                if (!VALID_TYPES.containsKey(it.contentType)) {
                    throw BadRequestException("Invalid MIME-TYPE for file: ${it.originalFilename}")
                }

                val uid = UUID.randomUUID().toString()
                val filename = "$uid.${VALID_TYPES[it.contentType]}"
                val path = Paths.get(RESOURCE_FOLDER, filename)

                Files.write(path, it.bytes)

                urls.add(path.toString())
            }
        })

        return urls
    }

}