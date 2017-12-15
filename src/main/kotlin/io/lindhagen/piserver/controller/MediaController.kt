package io.lindhagen.piserver.controller

import io.lindhagen.piserver.exception.BadRequestException
import io.lindhagen.piserver.exception.NotFoundException
import io.lindhagen.piserver.model.Media
import io.lindhagen.piserver.model.Season
import io.lindhagen.piserver.repository.MediaRepository
import io.lindhagen.piserver.repository.SeasonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    18.10.2017
 */
@RestController
@RequestMapping("/api/v1/media/")
class MediaController {

    @Autowired
    lateinit var repo: MediaRepository

    @Autowired
    lateinit var seasonRepo: SeasonRepository

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody media: Media): Media = repo.save(media.validate())

    @GetMapping("")
    fun list(@RequestParam(required = false) type: String?): Iterable<Media> {

        if (type.isNullOrEmpty()) {
            return repo.findAll()
        }

        return repo.findByType(type!!)
    }

    @GetMapping("{id}")
    fun retrieve(@PathVariable(name="id") id: Long): Media {
        var media: Media? = repo.findOne(id) ?:
                throw NotFoundException("Could not find media with id: $id")

        if (media!!.isTvShow()) {
            var s = mutableListOf<Season>()

            seasonRepo
                    .findByMediaId(media.id)
                    .forEach { it -> s.add(it) }

            media.seasons = s
        }

        return media
    }

    @GetMapping("/search")
    fun search(@RequestParam(name = "query") query: String?): List<Media?> =
            repo.findByTitleContaining(query ?: "")

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody media: Media): Media {
        if (media.id == null || media.id < 0) {
            throw BadRequestException("Media must exist to be able to update (missing field `id`)")
        }

        if (media.id != id) {
            throw BadRequestException("Media Id (${media.id}) doesn't match Id ($id) in url")
        }

        return repo.save(media.validate())
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long): Map<String, Boolean> {
        repo.delete(id)

        return mapOf("success" to true)
    }
}