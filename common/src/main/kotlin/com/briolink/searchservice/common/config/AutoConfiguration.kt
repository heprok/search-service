package com.briolink.searchservice.common.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = [
        "com.briolink.searchservice.common.service",
        "com.briolink.searchservice.common.dto",
        "com.briolink.searchservice.common.config"
    ]
)
class AutoConfiguration
