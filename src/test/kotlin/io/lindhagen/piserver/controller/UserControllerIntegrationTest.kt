package io.lindhagen.piserver.controller

import io.lindhagen.piserver.model.User
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.*

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    15/12/2017
 */
class UserControllerIntegrationTest: AbstractControllerIntegrationTest<User>() {

    lateinit var validUser: User

    override fun getBaseUri() = "/api/v1/users/"

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
    fun `GET user, expect HTTP 200 if authenticated`() {
        performUserRegistration(validUser)

        val result = performAuthAndGetUser(validUser)

        assertHttpStatus(HttpStatus.OK, result)
    }

    /**
     * Ensure hashed password isn't leaked to user
     *
     * */
    @Test
    fun `GET user, expect password to be empty`() {
        performUserRegistration(validUser)

        val result = performAuthAndGetUser(validUser)

        assertEquals("", result.body.password)
    }

    @Test
    fun `GET user, expect HTTP 403 if not authenticated`() {
        performUserRegistration(validUser)

        val result = template.getForEntity(getBaseUri(), User::class.java)

        assertHttpStatus(HttpStatus.FORBIDDEN, result)
    }

    @Test
    fun `POST expect HTTP 201 on successful creation`() {
        val result = performUserRegistration(validUser)

        assertHttpStatus(HttpStatus.CREATED, result)
    }

    /**
     * "username" is a required field for user authentication,
     * for iBok this should probably be replaced with email
     * */
    @Test
    fun `POST Expect HTTP 400 when "username" is omitted` () {
        val u = validUser.apply { username = "" }

        expectBadRequestOnInvalidUser(u)
    }

    /**
     * Response should return `HTTP 400` when
     * `confirmPassword` is empty
     * */
    @Test
    fun `POST Expect HTTP 400 when "confirmPassword" is omitted`() {
        val u = validUser.apply { confirmPassword = null }

        expectBadRequestOnInvalidUser(u)
    }

    /**
     * Server should respond with `HTTP 400` if
     * `password` and `confirmPassword` doesn't match.
     * */
    @Test
    fun `POST Expect HTTP 400 when "password" and "confirmPassword" do not match`() {
        val u = validUser.apply { confirmPassword = "SnowingJon" }

        expectBadRequestOnInvalidUser(u)
    }

    /**
     * Hashed and unhashed versions of the password
     * should never be returned to the user.
     * Ensure this doesn't happen on Create.
     * */
    @Test
    fun `POST Expect "password" and "confirmPassword" to be empty when returned`() {
        val result = performUserRegistration(validUser)

        assertNotNull(result)
        assertTrue(result.body.password.isNullOrEmpty())
        assertTrue(result.body.confirmPassword.isNullOrEmpty())
    }

    /**
     * Multiple users cannot register with identical usernames (later emails),
     * as this breaks the authentication flow.
     * */
    @Test
    fun `POST Expect HTTP 400 on duplicate "username"`() {
        performUserRegistration(validUser) // Should be fine, implied by other test
        val r2 = performUserRegistration(validUser)

        assertNotNull(r2)
        assertEquals(HttpStatus.BAD_REQUEST, r2.statusCode)
    }

    /**
     * When creating a user from the open API-endpoint, should the client not
     * be able to custom define the user's role. This should be set by the server
     * to the lowest access level, to prevent security risks.
     * */
    @Test
    fun `POST Expect user's role to not be "ADMIN" or "DEVELOPER"` () {
        // TODO:ffl - Implement roles for users, with at least 3 levels STANDARD, SUPPORT, ADMIN.
        // TODO:ffl - Must also comply with Spring Boot Security
    }

    /**
     * Tests that the response will give HTTP 400 Bad Request,
     * if providing invalid values for the user.
     *
     * @throws: AssertionException
     * */
    protected fun expectBadRequestOnInvalidUser(user: User) {
        val result = performUserRegistration(user)

        assertBadRequest(result)
    }

    /**
     * Performs POST request to the user registration endpoint,
     * with the provided args.
     *
     * */
    fun performUserRegistration(user: User): ResponseEntity<User> =
        template.postForEntity(getBaseUri(), user, User::class.java)

    /**
     * Will authenticate the provided user,
     * and do Retrieve (R in CRUD) with the returned authorization token.
     *
     * > N.B. This method expects no errors in authentication
     * */
    fun performAuthAndGetUser(user: User): ResponseEntity<User> {
        val auth = template.postForEntity("/api/v1/auth/token/", user, User::class.java)

        val token = auth.headers["Authorization"]!![0]

        // Equivalence of getForEntity, just with HTTP-headers
        return template.exchange(
            getBaseUri(),
            HttpMethod.GET,
            HttpEntity<HttpHeaders>(createAuthorizationHeader(token)),
            User::class.java
        )
    }
}