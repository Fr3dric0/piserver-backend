package io.lindhagen.piserver.controller

import io.lindhagen.piserver.model.User
import io.lindhagen.piserver.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.security.Principal

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19.10.2017
 */
@RestController
@RequestMapping("/api/v1/users/")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("")
    fun retrieve(principal: Principal) = userService
        .findByUsernameOrThrowNotFound(principal.name)
        .apply { password = "" }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody user: User) = userService
        .create(user)
        .apply {
            password = ""
            confirmPassword = null
        }
}