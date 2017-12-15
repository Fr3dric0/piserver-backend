package io.lindhagen.piserver.model

import io.lindhagen.piserver.exception.BadRequestException
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    18.10.2017
 */
@Entity
@Table(name = "media")
data class Media(
        @Column(nullable = false)
        val title: String?,

        var subtitle: String?,

        var description: String?,

        @Column(nullable = false)
        val type: String?,

        // Thumbnail image
        val thumbnail: String?,

        // Used only when type is movie
        var url: String?,

        var rating: Float?,

        // Cover image
        val cover: String?,

        var released: LocalDateTime?,

        @OneToMany(mappedBy = "media", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
        var seasons: List<Season>
): AbstractEntity() {


    fun isTvShow() = this.type == "tv-show"
}