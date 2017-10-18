package io.lindhagen.piserver.config

import io.lindhagen.piserver.security.JWTAuthenticationFilter
import io.lindhagen.piserver.security.JWTLoginFilter
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    18.10.2017
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/api/v1/", "/api/v1").permitAll()
                // Allow registration and authentication
                .antMatchers(HttpMethod.POST, "/api/v1/users/").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/auth/token/").permitAll()

                .anyRequest().authenticated()
                .and()
                .addFilterBefore(JWTAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter::class.java
                )
                .addFilterBefore(
                        JWTLoginFilter("/api/v1/auth/token/", authenticationManager()),
                        UsernamePasswordAuthenticationFilter::class.java
                )
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("password")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("password")
                .roles("USER")
    }
}