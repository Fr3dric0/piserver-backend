package io.lindhagen.piserver.repository

import io.lindhagen.piserver.model.Episode
import org.springframework.data.repository.CrudRepository

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19.10.2017
 */
interface EpisodeRepository : CrudRepository<Episode, Long> {
}