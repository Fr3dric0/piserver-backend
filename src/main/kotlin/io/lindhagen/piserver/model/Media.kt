package io.lindhagen.piserver.model

import javax.persistence.*

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    18.10.2017
 */
@Entity
@Table(name = "media")
data class Media(
        var title: String,
        var subtitle: String?,
        var description: String?,
        var type: String
): AbstractEntity()