package io.lindhagen.piserver.service

import io.lindhagen.piserver.exception.BadRequestException
import io.lindhagen.piserver.exception.NotFoundException
import io.lindhagen.piserver.model.Media
import io.lindhagen.piserver.repository.MediaRepository
import io.lindhagen.piserver.repository.SeasonRepository
import io.lindhagen.piserver.util.conformToRange
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    15/12/2017
 */
@Service
class MediaService {

    @Autowired
    lateinit var repo: MediaRepository

    @Autowired
    lateinit var seasonRepo: SeasonRepository

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    val VALID_MEDIATYPES = listOf("tv-show", "movie")

    /**
     * Finds all instances, but also allows to filter on media-type
     * */
    fun findAll(type: String? = null): Iterable<Media> =
        if (!type.isNullOrEmpty()) {
            repo.findByType(type!!)
        } else {
            repo.findAll()
        }

    /**
     * @throws io.lindhagen.piserver.exception.NotFoundException
     * */
    fun findByIdOrThrowNotFound(id: Long): Media {
        val media = repo.findOne(id) ?: throw NotFoundException("Cannot find media with id: $id")

        media.seasons = if (media.isTvShow()) {
            seasonRepo.findByMediaId(media.id).toList()
        } else {
            emptyList()
        }

        return media
    }

    /**
     * Updates an existing instance, and returns the updated instance
     * @throws io.lindhagen.piserver.exception.NotFoundException,
     *         io.lindhagen.piserver.exception.BadRequestException
     * */
    fun update(id: Long, media: Media): Media {
        if (!repo.exists(id)) {
            throw NotFoundException("Cannot update unknown media with id: $id")
        }

        validateMedia(media)

        media.rating = media.rating?.let { conformRatingToRange(it) }

        return repo.save(media)
    }

    fun create(media: Media): Media {
        validateMedia(media)

        media.rating = media.rating?.let { conformRatingToRange(it) }

        return repo.save(media)
    }

    fun delete(id: Long): Any? {
        if (!repo.exists(id)) {
            log.debug("Trying to delete non-existing media (id: $id)")
        }

        repo.delete(id)

        return null
    }

    /**
     * Ensures the media instance only contains legal values
     * @throws io.lindhagen.piserver.exception.BadRequestException
     * */
    fun validateMedia(media: Media) {
        if (media.title.isNullOrEmpty()) {
            throw BadRequestException("Required field `title` cannot be empty")
        }

        if (media.type.isNullOrEmpty()) {
            throw BadRequestException("Required field `type` cannot be empty")
        }

        if (media.type !in VALID_MEDIATYPES) {
            throw BadRequestException(
                "Illegal media-type: ${media.type}. Legal values: ${VALID_MEDIATYPES.joinToString(", ")}")
        }

        // Prevent movies from containing seasons
        if (!media.isTvShow() && media.seasons.isNotEmpty()) {
            throw BadRequestException("Non-tv-show media cannot contain seasons")
        }

        // Prevent tv-show from containing movie `url`
        if (media.isTvShow() && !media.url.isNullOrEmpty()) {
            throw BadRequestException("Tv-show cannot contain movie url")
        }
    }

    /**
     * Ensures the rating stays between 0f and 10f
     * */
    private fun conformRatingToRange(rating: Float) = rating.conformToRange(0f, 10f)
}