package io.lindhagen.piserver.security

import io.lindhagen.piserver.model.User
import org.springframework.security.core.userdetails.UserDetailsService

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19.10.2017
 */
interface UserService: UserDetailsService {

    fun findByUsername(username: String): User
}