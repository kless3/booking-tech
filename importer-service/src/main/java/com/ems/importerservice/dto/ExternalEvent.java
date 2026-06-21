package com.ems.importerservice.dto;

import com.ems.importerservice.domain.EventSource;
import java.time.LocalDateTime;

public record ExternalEvent(
    EventSource source,
    String externalId,
    String title,
    String description,
    String location,
    LocalDateTime startsAt,
    int capacity
) {
}
