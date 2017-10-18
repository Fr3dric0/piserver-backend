package io.lindhagen.piserver

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class PiServerApplication

fun main(args: Array<String>) {
    SpringApplication.run(PiServerApplication::class.java, *args)
}
