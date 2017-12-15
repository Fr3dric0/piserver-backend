package io.lindhagen.piserver.service

import io.lindhagen.piserver.exception.BadRequestException
import io.lindhagen.piserver.exception.NotFoundException
import io.lindhagen.piserver.model.User
import io.lindhagen.piserver.repository.UserRepository
import io.lindhagen.piserver.util.containsLowerCaseLetters
import io.lindhagen.piserver.util.containsNumbers
import io.lindhagen.piserver.util.containsUpperCaseLetters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    03/12/2017
 */
@Service
class UserService {

    @Autowired
    lateinit var repository: UserRepository

    @Autowired
    lateinit var bcrypt: BCryptPasswordEncoder

    /**
     * Attempts to find user by it's username,
     * and gives HTTP 404 if not found
     *
     * @author Fredrik F. Lindhagen <fred.lindh96@gmail.com>
     * @throws no.ibok.server.exception.NotFoundException
     * */
    fun findByUsernameOrThrowNotFound(username: String) =
        repository.findByUsername(username) ?:
            throw NotFoundException("Cannot find user with username `$username`")

    /**
     * Creates a new user
     *
     * @author Fredrik F. Lindhagen <fred.lindh96@gmail.com>
     * @param user The user instance to save in the database
     * @param skipPasswordCheck Password can be null if authenticated through Third party
     * @throws no.ibok.server.exception.BadRequestException
     * */
    fun create(user: User, skipPasswordCheck: Boolean = false): User {
        validateUser(user, requireConfirmPassword = true)
        requireNoDuplicateUser(user)

        user.password = bcrypt.encode(user.password)

        return repository.save(user)
    }

    /**
     * Updates an existing user
     *
     * @author Fredrik F. Lindhagen <fred.lindh96@gmail.com>
     * @param user User to update
     * @throws  no.ibok.server.exception.BadRequestException,
     *          no.ibok.server.exception.NotFoundException
     * */
    fun update(user: User): User {
        // If changes has been made to the password fields (i.e. they are not empty)
        // validation should be done
        val validatePassword = !user.password.isNullOrEmpty() || !user.confirmPassword.isNullOrEmpty()

        validateUser(user, requireConfirmPassword = validatePassword)
        requireUserExists(user)

        if (validatePassword) {
            user.password = bcrypt.encode(user.password)
        }

        return repository.save(user)
    }

    /**
     * Validates the properties of a user,
     * and ensures no invalid data is passed on
     * @param user The user
     * @param allowDuplicates Should multiple user instances be allowed?
     * @param skipPasswordCheck Should password validation be ignored?
     * @param requireConfirmPassword If confirm password must be present
     * */
    private fun validateUser(user: User,
                             skipPasswordCheck: Boolean = false,
                             requireConfirmPassword: Boolean = false) {
        if (user.username.isNullOrEmpty()) {
            throwBadRequestForMissingField("username")
        }

        if (!skipPasswordCheck) {
            if (user.password.isNullOrEmpty()) {
                throwBadRequestForMissingField("password")
            }

            if (requireConfirmPassword) {
                if (user.confirmPassword.isNullOrEmpty()) {
                    throwBadRequestForMissingField("confirmPassword")
                }

                if (!user.confirmPassword.equals(user.password)) {
                    throw BadRequestException("Fields `password` and `confirmPassword` do not match")
                }
            }

            validatePassword(user.password!!)
        }
    }

    /**
     * Checks if user with identical username already exists,
     * and throws exception (HTTP 400) if found.
     * @throws no.ibok.server.exception.BadRequestException
     * */
    private fun requireNoDuplicateUser(user: User) {
        if (repository.existsByUsername(user.username!!)) {
            throw BadRequestException("User with username `${user.username}` already exists")
        }
    }

    /**
     * Complains if user doesn't exist in database
     * @throws no.ibok.server.exception.NotFoundException
     * */
    private fun requireUserExists(user: User) {
        if (!repository.existsByUsername(user.username!!)) {
            throw NotFoundException("User with username `${user.username}` doesn't exist")
        }
    }

    /**
     * Validates that password upholds some requirements
     *  - length >= 8
     *  - contains numbers
     *  - has big and small letters
     *
     *  @throws io.lindhagen.piserver.exception.BadRequestException
     * */
    private fun validatePassword(password: String) {
        if (password.length < 8) {
            throw BadRequestException("Password is too short, must be >= 8, current length is `${password.length}`")
        }

        if (!password.containsNumbers()) {
            throw BadRequestException("Password must contain numbers")
        }

        if (!password.containsUpperCaseLetters() || !password.containsLowerCaseLetters()) {
            throw BadRequestException("Password must contain both uppercase and lowercase letters")
        }
    }

    /**
     * Helper function to throw Bad Request exceptions,
     * with standard word formulations
     * */
    private fun throwBadRequestForMissingField(field: String) {
        throw BadRequestException("Missing required field: `$field`")
    }
}