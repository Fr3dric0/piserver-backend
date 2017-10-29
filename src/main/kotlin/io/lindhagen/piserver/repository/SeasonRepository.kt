package io.lindhagen.piserver.repository

import io.lindhagen.piserver.model.Media
import io.lindhagen.piserver.model.Season
import org.springframework.data.repository.CrudRepository

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19.10.2017
 */
interface SeasonRepository : CrudRepository<Season, Long>{

    fun findByMediaId(mediaId: Long): Iterable<Season>
}