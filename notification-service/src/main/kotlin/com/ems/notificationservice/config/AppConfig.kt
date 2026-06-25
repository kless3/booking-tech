package com.ems.notificationservice.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.EnableMongoAuditing

@Configuration
@EnableMongoAuditing
@EnableConfigurationProperties(KafkaTopicsProperties::class, NotificationDeliveryProperties::class)
class AppConfig
