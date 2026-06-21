package com.ems.importerservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateEventRequest(
    UUID organizerUserId,
    String title,
    String description,
    String location,
    LocalDateTime startsAt,
    int capacity,
    String organizerNote
) {
}
