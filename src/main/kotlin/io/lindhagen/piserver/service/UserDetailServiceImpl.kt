package io.lindhagen.piserver.service

import io.lindhagen.piserver.model.User
import io.lindhagen.piserver.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * An implementation of UserDetailsService, used by
 * io.lindhagen.piserver.config.WebSecurityConfig.
 *
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19/11/2017
 */
@Service
class UserDetailServiceImpl: UserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository

    /**
     *
     *
     * */
    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userRepository.findByUsername(username) ?:
            throw UsernameNotFoundException("Cannot find user with email $username")

        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            listOf()
        )
    }
}