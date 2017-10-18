package io.lindhagen.piserver.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    18.10.2017
 */
@RestController
@RequestMapping("/api/v1")
class Dashboard {

    @GetMapping("")
    fun index() = mapOf(
            "/api/v1/media" to mapOf(
                    "GET" to "List all media items",
                    "POST" to "Create a new media"
            ),
            "/api/v1/media/:id" to mapOf(
                    "PUT" to "Update an existing media",
                    "DELETE" to "Delete a specific media"
            ),
            "/api/v1/users" to mapOf(),
            "/api/v1/users/:id" to mapOf(),
            "/api/v1/auth/token" to mapOf(
                    "POST" to "Authenticates a user, returns JWT token"
            )
    )
}