package io.lindhagen.piserver.model

import org.hibernate.validator.constraints.Email
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19.10.2017
 */
@Entity
data class User(
        val name: String?,

        @Column(nullable = false, unique = true)
        @Email
        val email: String,

        @Column(nullable = false)
        internal val password: String,

        @ManyToOne
        @JoinColumn(name = "role_id")
        val role: Role

): UserDetails, AbstractEntity() {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf<GrantedAuthority>(
                SimpleGrantedAuthority(role.name)
        )
    }

    override fun isEnabled() = true

    override fun getUsername(): String = email

    override fun isCredentialsNonExpired() = true

    override fun getPassword(): String = password

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true
}