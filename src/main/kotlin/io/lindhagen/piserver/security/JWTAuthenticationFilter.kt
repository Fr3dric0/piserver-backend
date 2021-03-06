package io.lindhagen.piserver.security;

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.lindhagen.piserver.model.User
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.util.Date
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * ref. https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/
 *
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    12/11/2017
 */
class JWTAuthenticationFilter : UsernamePasswordAuthenticationFilter {

    constructor(authenticationManager: AuthenticationManager) : super() {
        this.authenticationManager = authenticationManager
        setFilterProcessesUrl("/api/v1/auth/token/")
    }

    /**
     * Parses the users credentials and issue them to the authentication manager
     * @throws AuthenticationException
     * */
    override fun attemptAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse): Authentication {

        try {
            val creds: User = ObjectMapper() // Map post-body to User model
                .readValue(req.inputStream, User::class.java)

            return authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    creds.username,
                    creds.password,
                    arrayListOf()
                ))
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }


    /**
     * Called when a user has been successfully authenticated
     *
     * */
    override fun successfulAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain,
        auth: Authentication) {

        val user = auth.principal as org.springframework.security.core.userdetails.User

        val token: String = Jwts.builder()
            .setSubject(user.username)
            .setExpiration(Date(System.currentTimeMillis() + 860000))
            .signWith(SignatureAlgorithm.HS512, "sdfsdsds")
            .compact()

        res.addHeader("Authorization", "Bearer $token")
    }
}