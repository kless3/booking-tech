package com.ems.importerservice.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableMongoAuditing
@EnableConfigurationProperties({EventServiceClientProperties.class, ImportProperties.class})
public class AppConfig {
}
