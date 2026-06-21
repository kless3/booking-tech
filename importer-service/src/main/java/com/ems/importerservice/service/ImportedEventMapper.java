package com.ems.importerservice.service;

import com.ems.importerservice.domain.ImportedEvent;
import com.ems.importerservice.dto.ImportedEventResponse;

public final class ImportedEventMapper {
    private ImportedEventMapper() {
    }

    public static ImportedEventResponse toResponse(ImportedEvent importedEvent) {
        return new ImportedEventResponse(
            importedEvent.getId(),
            importedEvent.getSource(),
            importedEvent.getExternalId(),
            importedEvent.getEventId(),
            importedEvent.getTitle(),
            importedEvent.getStatus(),
            importedEvent.getFailureReason(),
            importedEvent.getImportedAt(),
            importedEvent.getCreatedAt(),
            importedEvent.getUpdatedAt()
        );
    }
}
