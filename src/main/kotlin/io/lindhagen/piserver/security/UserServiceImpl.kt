package io.lindhagen.piserver.security

import io.lindhagen.piserver.model.User
import io.lindhagen.piserver.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19.10.2017
 */
@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var repo: UserRepository

    override fun findByUsername(username: String) = repo.findByEmail(username)

    override fun loadUserByUsername(username: String): UserDetails {
        val user = repo.findByEmail(username)

        if (user == null) {
            throw UsernameNotFoundException("User not found")
        }

        return user
    }
}