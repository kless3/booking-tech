package com.ems.importerservice.dto;

import com.ems.importerservice.domain.EventSource;
import com.ems.importerservice.domain.ImportStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record ImportedEventResponse(
    UUID id,
    EventSource source,
    String externalId,
    UUID eventId,
    String title,
    ImportStatus status,
    String failureReason,
    LocalDateTime importedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
