package io.lindhagen.piserver.controller

import io.lindhagen.piserver.exception.BadRequestException
import io.lindhagen.piserver.exception.NotFoundException
import io.lindhagen.piserver.model.Episode
import io.lindhagen.piserver.model.Media
import io.lindhagen.piserver.model.Season
import io.lindhagen.piserver.repository.EpisodeRepository
import io.lindhagen.piserver.repository.MediaRepository
import io.lindhagen.piserver.repository.SeasonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19.10.2017
 */
@RestController
@RequestMapping("/api/v1/media/{mediaId}/")
class TvShowController {

    @Autowired
    lateinit var mediaRepo: MediaRepository

    @Autowired
    lateinit var seasonRepo: SeasonRepository

    @Autowired
    lateinit var episodeRepo: EpisodeRepository

    @PostMapping("seasons/")
    fun addSeason(@PathVariable mediaId: Long, @RequestBody season: Season): Season {
        val media = mediaRepo.findOne(mediaId) ?:
                throw NotFoundException("Cannot find media with id `$mediaId`")

        mediaTypeMustBeTvShow(media)

        season.media = media

        return seasonRepo.save(season)
    }

    @PostMapping("seasons/{seasonId}/episodes/")
    fun addEpisode(@PathVariable mediaId: Long,
                   @PathVariable seasonId: Long,
                   @RequestBody episode: Episode): Episode {
        val season = seasonRepo.findOne(seasonId) ?:
                throw NotFoundException("Cannot find season with id `$seasonId`")

        episode.season = season

        return episodeRepo.save(episode)
    }

    private fun mediaTypeMustBeTvShow(media: Media) {
        if (!media.isTvShow()) {
            throw BadRequestException("Cannot add `season` to a non tv-show type")
        }
    }
}