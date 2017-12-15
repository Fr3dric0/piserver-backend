package io.lindhagen.piserver.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 * TODO:ffl - Move Secret to a .properties file,
 * which again is stored as an ENV variable
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    12/11/2017
 */
object TokenAuthenticationService {

    private const val EXPIRATION_TIME:Long = 864000000
    const val SECRET = "SomeGoodSecret"
    const val TOKEN_PREFIX = "Bearer"
    const val HEADER_STRING = "Authorization"

    /**
     * Creates the Json Web Token, and returns it in the Header and Body
     * @throws: IOException
     * */
    fun addAuthentication(res: HttpServletResponse, username: String) {
       val jwt = Jwts.builder()
               .setSubject(username)
               .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
               .signWith(SignatureAlgorithm.HS512, SECRET)
               .compact()

        res.addHeader(HEADER_STRING, "${TOKEN_PREFIX} $jwt")
        res.addHeader("Content-Type", "application/json")
        res.writer.write("{\"token\":\"$jwt\"}")
    }

    fun getAuthentication(req: HttpServletRequest): Authentication? {
        val token: String? = req.getHeader(HEADER_STRING)

        // TODO:ffl - Use Kotlins null safety syntax

        token?.let {
            val user: String? = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .body
                    .subject

            return UsernamePasswordAuthenticationToken(user, null, Collections.emptyList())
        }

        return null
    }
}