package io.lindhagen.piserver.controller

import io.lindhagen.piserver.model.User
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    15/12/2017
 */
class AuthenticationIntegrationTest: AbstractControllerIntegrationTest<User>() {

    override fun getBaseUri() = "/api/v1/auth/token/"

    lateinit var validUser: User

    val TOKEN_PREFIX = "Bearer"

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Before
    fun setUp() {
        validUser = User(
            name = "Jon Snow",
            username = "jon.snow@westeros.com",
            password = "SnowingJon2",
            confirmPassword = "SnowingJon2"
        )

        deleteUser(validUser)
    }

    @Test
    @Ignore
    fun `Authenticate, expect HTTP 200 on success`() {
        createTestUser(validUser)

        val res = template.postForEntity(AUTH_ENDPOINT, validUser, User::class.java)

        println("\n\n\n")
        println(res)
        println(res.statusCode)
        println("\n\n\n")

        assertHttpStatus(HttpStatus.OK, res)
    }

    @Test
    @Ignore
    fun `Authenticate, expect JWT in header on success`() {
        createTestUser(validUser)

        val res = performAuthentication(validUser)


        assertNotNull("Expect HTTP header to have Authorization", res.headers["Authorization"]!!)

        val auth = res.headers["Authorization"]!![0]

        assertTrue(
            "Authorization is prefixed with `$TOKEN_PREFIX `",
            auth.startsWith("Bearer "))

        assertTrue(
            "Authorization to have token",
            auth.replace("$TOKEN_PREFIX ", "").isNotEmpty())

        val token = auth.replace("$TOKEN_PREFIX ", "")
        assertTrue("JWT is properly formatted", token.split(".").size == 2)
    }

    @Test
    fun `Expect 401 on invalid or malformed token`() {

    }

    @Test
    fun `Expect 403 on invalid credentials`() {

    }

    @Test
    fun `Expect 401 on expired token`() {

    }
}