package io.lindhagen.piserver.security

import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    19/11/2017
 */
class JWTAuthorizationFilter(authenticationManager: AuthenticationManager) :
    BasicAuthenticationFilter(authenticationManager) {

    /**
     * Do authorization of user
     *
     * */
    override fun doFilterInternal(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain) {

        val header = req.getHeader("Authorization")

        if (header == null || !header.startsWith("Bearer")) {
            chain.doFilter(req, res)
            return
        }

        val auth: UsernamePasswordAuthenticationToken? = getAuthentication(req)

        SecurityContextHolder.getContext().authentication = auth
        chain.doFilter(req, res)
    }

    /**
     *
     *
     * */
    private fun getAuthentication(req: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val token: String? = req.getHeader("Authorization")

        return token?.let {
            val user: String? = Jwts.parser()
                .setSigningKey("sdfsdsds")
                .parseClaimsJws(it.replace("Bearer", ""))
                .body
                .subject

            user // Chain the user down
        }.let {
            // Only run if user is not null
            UsernamePasswordAuthenticationToken(it, null, arrayListOf())
        }
    }
}