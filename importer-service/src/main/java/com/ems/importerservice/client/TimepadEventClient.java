package com.ems.importerservice.client;

import com.ems.importerservice.domain.EventSource;
import com.ems.importerservice.dto.ExternalEvent;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TimepadEventClient implements ExternalEventClient {
    @Override
    public EventSource source() {
        return EventSource.TIMEPAD;
    }

    @Override
    public List<ExternalEvent> fetchEvents(int limit) {
        return List.of(
            new ExternalEvent(
                source(),
                "timepad-architecture-meetup-2026",
                "Architecture Meetup 2026",
                "Imported from Timepad",
                "Krakow Tech Hub",
                LocalDateTime.now().plusDays(30),
                120
            ),
            new ExternalEvent(
                source(),
                "timepad-java-day-2026",
                "Java Day 2026",
                "Imported from Timepad",
                "Prague Congress Centre",
                LocalDateTime.now().plusDays(75),
                350
            )
        ).stream().limit(limit).toList();
    }
}
