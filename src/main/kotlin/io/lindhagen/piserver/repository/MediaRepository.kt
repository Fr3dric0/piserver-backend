package io.lindhagen.piserver.repository

import io.lindhagen.piserver.model.Media
import org.springframework.data.repository.CrudRepository

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    18.10.2017
 */
interface MediaRepository: CrudRepository<Media, Long> {

    fun findByTitle(title: String): Media
    fun findByType(type: String): Iterable<Media>
}