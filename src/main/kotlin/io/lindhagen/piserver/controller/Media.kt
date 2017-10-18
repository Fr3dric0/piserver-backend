package io.lindhagen.piserver.controller

import io.lindhagen.piserver.model.Media
import io.lindhagen.piserver.repository.MediaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    18.10.2017
 */
@RestController
@RequestMapping("/api/v1/media/")
class Media {

    @Autowired
    lateinit var repo: MediaRepository

    @PostMapping("")
    fun create(@RequestBody media: Media): Media {
        val m = repo.save(media)

        return m
    }

    @GetMapping("")
    fun list() = repo.findAll()

    @GetMapping("{id}")
    fun retrieve(@PathVariable(name="id") id: Long) = repo.findOne(id)

    @GetMapping("/search")
    fun search(@RequestParam(name = "query") query: String?): String {

        return query ?: "No search term 😞"
    }
}