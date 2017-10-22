package io.lindhagen.piserver.model

import io.lindhagen.piserver.exception.BadRequestException
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

        val rating: String?,

        // Cover image
        val cover: String?,

        @OneToMany(mappedBy = "media", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
        val seasons: List<Season>
): AbstractEntity() {


    fun isTvShow() = this.type == "tv-show"

    /**
     * Handles Basic validation of models
     * @throws BadRequestException
     * */
    fun validate (): Media {
        val validTypes = listOf("movie", "tv-show")

        this.title ?: throw BadRequestException("Missing required field `title`")
        val type = this.type ?: throw BadRequestException("Missing required field `type`")

        if (!validTypes.contains(type)) {
            throw BadRequestException("Invalid value $type in `type`, legal values [${validTypes.joinToString(", ")}]")
        }

        return this
    }
}