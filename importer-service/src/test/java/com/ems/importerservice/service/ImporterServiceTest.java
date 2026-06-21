package com.ems.importerservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ems.importerservice.client.EventServiceClient;
import com.ems.importerservice.client.ExternalEventClient;
import com.ems.importerservice.client.ExternalEventClientRegistry;
import com.ems.importerservice.domain.EventSource;
import com.ems.importerservice.domain.ImportStatus;
import com.ems.importerservice.domain.ImportedEvent;
import com.ems.importerservice.dto.CreateEventRequest;
import com.ems.importerservice.dto.EventResponse;
import com.ems.importerservice.dto.ExternalEvent;
import com.ems.importerservice.dto.ImportRunResponse;
import com.ems.importerservice.exception.EventServiceUnavailableException;
import com.ems.importerservice.repository.ImportedEventRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ImporterServiceTest {
    private final ImportedEventRepository importedEventRepository = Mockito.mock(ImportedEventRepository.class);
    private final EventServiceClient eventServiceClient = Mockito.mock(EventServiceClient.class);
    private final ExternalEventClient externalEventClient = new TestExternalEventClient();
    private final ImporterService importerService = new ImporterService(
        importedEventRepository,
        new ExternalEventClientRegistry(List.of(externalEventClient)),
        eventServiceClient
    );

    @Test
    void importsExternalEventsIntoEventService() {
        UUID organizerUserId = UUID.randomUUID();
        when(importedEventRepository.existsBySourceAndExternalId(EventSource.TICKETMASTER, "external-1")).thenReturn(false);
        when(eventServiceClient.createEvent(any(CreateEventRequest.class))).thenReturn(eventResponse(organizerUserId));
        when(importedEventRepository.save(any(ImportedEvent.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ImportRunResponse response = importerService.importEvents(EventSource.TICKETMASTER, organizerUserId, 1);

        assertEquals(1, response.imported());
        assertEquals(0, response.skipped());
        assertEquals(0, response.failed());
        assertEquals(ImportStatus.IMPORTED, response.records().getFirst().status());
        verify(eventServiceClient).createEvent(any(CreateEventRequest.class));
    }

    @Test
    void skipsAlreadyImportedExternalEvents() {
        UUID organizerUserId = UUID.randomUUID();
        when(importedEventRepository.existsBySourceAndExternalId(EventSource.TICKETMASTER, "external-1")).thenReturn(true);

        ImportRunResponse response = importerService.importEvents(EventSource.TICKETMASTER, organizerUserId, 1);

        assertEquals(0, response.imported());
        assertEquals(1, response.skipped());
        assertEquals(0, response.failed());
        verify(eventServiceClient, never()).createEvent(any(CreateEventRequest.class));
    }

    @Test
    void storesFailedImportWhenEventServiceFails() {
        UUID organizerUserId = UUID.randomUUID();
        when(importedEventRepository.existsBySourceAndExternalId(EventSource.TICKETMASTER, "external-1")).thenReturn(false);
        when(eventServiceClient.createEvent(any(CreateEventRequest.class)))
            .thenThrow(new EventServiceUnavailableException("Event Service request failed"));
        when(importedEventRepository.save(any(ImportedEvent.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ImportRunResponse response = importerService.importEvents(EventSource.TICKETMASTER, organizerUserId, 1);

        assertEquals(0, response.imported());
        assertEquals(0, response.skipped());
        assertEquals(1, response.failed());
        assertEquals(ImportStatus.FAILED, response.records().getFirst().status());
    }

    private EventResponse eventResponse(UUID organizerUserId) {
        return new EventResponse(
            UUID.randomUUID(),
            organizerUserId,
            "Imported Event",
            "Imported",
            "Warsaw",
            LocalDateTime.now().plusDays(30),
            100,
            0,
            "PUBLISHED"
        );
    }

    private static class TestExternalEventClient implements ExternalEventClient {
        @Override
        public EventSource source() {
            return EventSource.TICKETMASTER;
        }

        @Override
        public List<ExternalEvent> fetchEvents(int limit) {
            return List.of(
                new ExternalEvent(
                    EventSource.TICKETMASTER,
                    "external-1",
                    "Imported Event",
                    "Imported",
                    "Warsaw",
                    LocalDateTime.now().plusDays(30),
                    100
                )
            ).stream().limit(limit).toList();
        }
    }
}
