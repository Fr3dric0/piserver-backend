package io.lindhagen.piserver.model

import javax.persistence.*
import kotlin.jvm.Transient

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19.10.2017
 */
@Entity
data class User(
        var name: String?,

        @Column(nullable = false, unique = true)
        var username: String,

        @Column(nullable = false)
        var password: String,

        @Transient
        var confirmPassword: String?
//
//        @ManyToOne
//        @JoinColumn(name = "role_id")
//        val role: Role

): AbstractEntity()