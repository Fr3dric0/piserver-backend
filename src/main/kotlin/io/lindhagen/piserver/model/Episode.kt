package io.lindhagen.piserver.model

import org.springframework.beans.factory.annotation.Value
import java.time.LocalDateTime
import javax.persistence.*

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19.10.2017
 */
@Entity
data class Episode(
        val episode: Int = 1,
        var description: String?,
        var thumbnail: String?,
        var cover: String?,

        @Column(name = "created_on", columnDefinition = "TIMESTAMP", updatable = false, insertable = false)
        @Value("NOW")
        val createdOn: LocalDateTime,

        @ManyToOne
        @JoinColumn(name = "season_id")
        var season: Season

) : AbstractEntity()