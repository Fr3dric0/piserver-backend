package io.lindhagen.piserver.model

import org.hibernate.validator.constraints.Email
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*
import kotlin.jvm.Transient

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19.10.2017
 */
@Entity
data class User(
        val name: String?,

        @Column(nullable = false, unique = true)
        val username: String,

        @Column(nullable = false)
        var password: String,

        @Transient
        var confirmPassword: String?
//
//        @ManyToOne
//        @JoinColumn(name = "role_id")
//        val role: Role

): AbstractEntity() {

//    fun getAuthorities(): MutableCollection<out GrantedAuthority> {
//        return mutableListOf<GrantedAuthority>(
//                SimpleGrantedAuthority(role.name)
//        )
//    }

    fun isEnabled() = true

    fun isCredentialsNonExpired() = true

    fun isAccountNonExpired() = true

    fun isAccountNonLocked() = true
}