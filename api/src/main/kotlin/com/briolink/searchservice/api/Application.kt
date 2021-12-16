package com.briolink.searchservice.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EntityScan(
    basePackages = [
        "com.briolink.searchservice.common.jpa.read.entity",
        "com.briolink.searchservice.common.jpa.write.entity",
    ],
)
@EnableJpaRepositories(
    basePackages = [
        "com.briolink.searchservice.common.jpa.write.repository",
        "com.briolink.searchservice.common.jpa.read.repository",
    ],
)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
