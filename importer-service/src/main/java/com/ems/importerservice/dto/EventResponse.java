package com.ems.importerservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponse(
    UUID id,
    UUID organizerUserId,
    String title,
    String description,
    String location,
    LocalDateTime startsAt,
    int capacity,
    int ticketsSold,
    String status
) {
}
