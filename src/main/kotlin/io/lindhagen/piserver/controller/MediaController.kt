package io.lindhagen.piserver.controller

import io.lindhagen.piserver.exception.BadRequestException
import io.lindhagen.piserver.exception.NotFoundException
import io.lindhagen.piserver.model.Media
import io.lindhagen.piserver.model.Season
import io.lindhagen.piserver.repository.MediaRepository
import io.lindhagen.piserver.repository.SeasonRepository
import io.lindhagen.piserver.service.MediaService
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
    lateinit var mediaService: MediaService

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody media: Media) =
        mediaService.create(media)

    @GetMapping("")
    fun list(@RequestParam(required = false) type: String?) =
        mediaService.findAll(type)

    @GetMapping("{id}")
    fun retrieve(@PathVariable(name="id") id: Long) =
        mediaService.findByIdOrThrowNotFound(id)

    @GetMapping("/search")
    fun search(@RequestParam(name = "query") query: String?): List<Media?> =
        repo.findByTitleContaining(query ?: "")

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody media: Media) =
        mediaService.update(id, media)

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = mediaService.delete(id)
}