package io.lindhagen.piserver.model

import org.springframework.beans.factory.annotation.Value
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19.10.2017
 */
@Entity
@Table(name = "season")
data class Season(
    val season: Int? = 1,
    var title: String?,
    var description: String?,

    var thumbnail: String?,
    var cover: String?,

    @Column(name="created_on", columnDefinition = "TIMESTAMP", updatable = false, insertable = false)
    @Value("NOW")
    val createdOn: LocalDateTime,

    var released: LocalDateTime?,

    @ManyToOne
    @JoinColumn(name="media_id")
    var media: Media,

    @OneToMany(mappedBy = "season", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
    var episodes: List<Episode>

): AbstractEntity()