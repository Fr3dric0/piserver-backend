package io.lindhagen.piserver.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

/**
 * @author: Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created: 16.10.2017
 */
public class TokenAuthenticationService {

    static final long EXPIRATION_TIME = 864_000_000; // 10 days
    static final String SECRET = "SomeGoodSecret";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    public static void addAuthentication(HttpServletResponse res, String username) throws IOException {
        String jwt = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + jwt);
        res.addHeader("Content-Type", "application/json");
        res.getWriter().write(String.format("{\"token\":\"%s\"}", jwt));
    }

    public static Authentication getAuthentication(HttpServletRequest req) {
        String token = req.getHeader(HEADER_STRING);

        if (token != null) {
            String user = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();



            if (user != null) {
                return new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        Collections.emptyList()
                );
            }
        }

        return null;
    }
}
