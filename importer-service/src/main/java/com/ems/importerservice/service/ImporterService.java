package com.ems.importerservice.service;

import com.ems.importerservice.client.EventServiceClient;
import com.ems.importerservice.client.ExternalEventClient;
import com.ems.importerservice.client.ExternalEventClientRegistry;
import com.ems.importerservice.domain.EventSource;
import com.ems.importerservice.domain.ImportedEvent;
import com.ems.importerservice.dto.CreateEventRequest;
import com.ems.importerservice.dto.EventResponse;
import com.ems.importerservice.dto.ExternalEvent;
import com.ems.importerservice.dto.ImportRunResponse;
import com.ems.importerservice.dto.ImportedEventResponse;
import com.ems.importerservice.exception.ImportedEventNotFoundException;
import com.ems.importerservice.repository.ImportedEventRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImporterService {
    private final ImportedEventRepository importedEventRepository;
    private final ExternalEventClientRegistry externalEventClientRegistry;
    private final EventServiceClient eventServiceClient;

    public ImporterService(
        ImportedEventRepository importedEventRepository,
        ExternalEventClientRegistry externalEventClientRegistry,
        EventServiceClient eventServiceClient
    ) {
        this.importedEventRepository = importedEventRepository;
        this.externalEventClientRegistry = externalEventClientRegistry;
        this.eventServiceClient = eventServiceClient;
    }

    @Transactional
    public ImportRunResponse importEvents(EventSource source, UUID organizerUserId, int limit) {
        ExternalEventClient client = externalEventClientRegistry.get(source);
        List<ImportedEventResponse> records = new ArrayList<>();
        int imported = 0;
        int skipped = 0;
        int failed = 0;

        for (ExternalEvent externalEvent : client.fetchEvents(limit)) {
            if (importedEventRepository.existsBySourceAndExternalId(source, externalEvent.externalId())) {
                skipped++;
                continue;
            }

            ImportedEvent importedEvent = new ImportedEvent(source, externalEvent.externalId(), externalEvent.title());
            try {
                EventResponse event = eventServiceClient.createEvent(toCreateEventRequest(externalEvent, organizerUserId));
                importedEvent.markImported(event.id(), LocalDateTime.now());
                imported++;
            } catch (RuntimeException exception) {
                importedEvent.markFailed(exception.getMessage() == null ? "Import failed" : exception.getMessage());
                failed++;
            }
            records.add(ImportedEventMapper.toResponse(importedEventRepository.save(importedEvent)));
        }

        return new ImportRunResponse(source, imported, skipped, failed, records);
    }

    @Transactional(readOnly = true)
    public List<ImportedEventResponse> getImportedEvents(EventSource source) {
        return importedEventRepository.findAllBySourceOrderByCreatedAtDesc(source).stream()
            .map(ImportedEventMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public ImportedEventResponse getImportedEvent(UUID id) {
        return importedEventRepository.findById(id)
            .map(ImportedEventMapper::toResponse)
            .orElseThrow(() -> new ImportedEventNotFoundException(id));
    }

    private CreateEventRequest toCreateEventRequest(ExternalEvent externalEvent, UUID organizerUserId) {
        return new CreateEventRequest(
            organizerUserId,
            externalEvent.title().trim(),
            externalEvent.description(),
            externalEvent.location().trim(),
            externalEvent.startsAt(),
            externalEvent.capacity(),
            "Imported from " + externalEvent.source() + " externalId=" + externalEvent.externalId()
        );
    }
}
