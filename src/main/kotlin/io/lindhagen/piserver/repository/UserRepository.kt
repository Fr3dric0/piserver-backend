package io.lindhagen.piserver.repository

import io.lindhagen.piserver.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19.10.2017
 */
@Repository
interface UserRepository: CrudRepository<User, Long> {

    fun findByEmail(email: String): User
}