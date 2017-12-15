package io.lindhagen.piserver.controller

import io.lindhagen.piserver.model.User
import io.lindhagen.piserver.repository.UserRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    02/12/2017
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractControllerIntegrationTest<Model> {

    @LocalServerPort
    protected lateinit var port: Integer // Ignore complaint

    @Autowired
    lateinit var template: TestRestTemplate

    @Autowired
    lateinit var userRepository: UserRepository

    protected abstract fun getBaseUri(): String

    protected val AUTH_ENDPOINT = "/api/v1/auth/token/"

    /**
     * Tries to authenticate a user, and returns the response.
     * */
    protected fun performAuthentication(user: User): ResponseEntity<User> =
        template.postForEntity(AUTH_ENDPOINT, user, User::class.java)

    /**
     * Creates an HttpHeaders and populates it with the Authorization header
     * */
    protected fun createAuthorizationHeader(authorization: String): HttpHeaders {
        val headers = HttpHeaders()
        headers.add("Authorization", authorization)

        return headers
    }

    /**
     * Creates a user through the UserRepository
     * */
    protected fun createTestUser(user: User): User =
        userRepository.save(user.apply {
            password = BCryptPasswordEncoder().encode(password)
        })

    /**
     * Deletes the user from the database,
     * if it exists
     * */
    protected fun deleteUser(user: User) {
        userRepository.findByUsername(user.username)?.let {
            userRepository.delete(it.id)
        }
    }

    /**
     * Asserts that HTTP 400 is returned
     * */
    protected fun assertBadRequest(result: ResponseEntity<Model>) {
        assertHttpStatus(HttpStatus.BAD_REQUEST, result)
    }

    /**
     * Asserts that a given Http status is returned.
     * Also asserts that results is not null
     * @throws: AssertionException
     * */
    protected fun assertHttpStatus(expectedStatus: HttpStatus, result: ResponseEntity<Model>) {
        assertNotNull(result)
        assertEquals(expectedStatus, result.statusCode)
    }

}