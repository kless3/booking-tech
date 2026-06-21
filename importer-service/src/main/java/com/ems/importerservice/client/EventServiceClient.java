package com.ems.importerservice.client;

import com.ems.importerservice.config.EventServiceClientProperties;
import com.ems.importerservice.dto.CreateEventRequest;
import com.ems.importerservice.dto.EventResponse;
import com.ems.importerservice.exception.EventServiceUnavailableException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class EventServiceClient {
    private final RestClient eventServiceRestClient;

    public EventServiceClient(RestClient.Builder builder, EventServiceClientProperties properties) {
        this.eventServiceRestClient = builder.baseUrl(properties.baseUrl()).build();
    }

    public EventResponse createEvent(CreateEventRequest request) {
        try {
            EventResponse response = eventServiceRestClient.post()
                .uri("/api/v1/events")
                .body(request)
                .retrieve()
                .body(EventResponse.class);
            if (response == null) {
                throw new EventServiceUnavailableException("Event Service returned an empty event response");
            }
            return response;
        } catch (RestClientException exception) {
            throw new EventServiceUnavailableException("Event Service request failed", exception);
        }
    }
}
