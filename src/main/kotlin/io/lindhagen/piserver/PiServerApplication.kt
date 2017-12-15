package io.lindhagen.piserver

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
class PiServerApplication {

    @Bean
    fun bcryptPasswordEncoder() = BCryptPasswordEncoder()
}

fun main(args: Array<String>) {
    SpringApplication.run(PiServerApplication::class.java, *args)
}
