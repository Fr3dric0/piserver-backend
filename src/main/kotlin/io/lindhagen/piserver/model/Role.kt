package io.lindhagen.piserver.model

import javax.persistence.Entity
import javax.persistence.OneToMany

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19.10.2017
 */
@Entity
data class Role(
        val name: String,

        @OneToMany(mappedBy = "role")
        val users: List<User>
) : AbstractEntity()