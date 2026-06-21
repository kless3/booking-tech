package com.ems.importerservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.clients.event-service")
public record EventServiceClientProperties(String baseUrl) {
}
