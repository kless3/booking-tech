package com.ems.importerservice.client;

import com.ems.importerservice.domain.EventSource;
import com.ems.importerservice.dto.ExternalEvent;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TicketmasterEventClient implements ExternalEventClient {
    @Override
    public EventSource source() {
        return EventSource.TICKETMASTER;
    }

    @Override
    public List<ExternalEvent> fetchEvents(int limit) {
        return List.of(
            new ExternalEvent(
                source(),
                "tm-kotlin-conf-2026",
                "Kotlin Conf 2026",
                "Imported from Ticketmaster",
                "Warsaw Expo XXI",
                LocalDateTime.now().plusDays(45),
                500
            ),
            new ExternalEvent(
                source(),
                "tm-cloud-summit-2026",
                "Cloud Summit 2026",
                "Imported from Ticketmaster",
                "Berlin Messe",
                LocalDateTime.now().plusDays(60),
                800
            )
        ).stream().limit(limit).toList();
    }
}
