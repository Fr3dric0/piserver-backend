package io.lindhagen.piserver.controller

import io.lindhagen.piserver.model.User
import io.lindhagen.piserver.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19.10.2017
 */
@RestController
@RequestMapping("/api/v1/users/")
class Users {

    @Autowired
    lateinit var repo: UserRepository

    @PostMapping("")
    fun register(@RequestBody user: User): User {

        return repo.save(user)
    }
}