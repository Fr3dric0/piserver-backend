package io.lindhagen.piserver.model

import javax.persistence.*

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    18.10.2017
 */
@Entity
@Table(name = "media")
data class Media(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = -1,
        var title: String,
        var subtitle: String?,
        var description: String?,
        var type: String = "movie"
) {
        constructor() : this(-1, "", "", "")
}