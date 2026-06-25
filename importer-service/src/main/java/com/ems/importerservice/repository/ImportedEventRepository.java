package com.ems.importerservice.repository;

import com.ems.importerservice.domain.EventSource;
import com.ems.importerservice.domain.ImportedEvent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImportedEventRepository extends MongoRepository<ImportedEvent, UUID> {
    boolean existsBySourceAndExternalId(EventSource source, String externalId);

    Optional<ImportedEvent> findBySourceAndExternalId(EventSource source, String externalId);

    List<ImportedEvent> findAllBySourceOrderByCreatedAtDesc(EventSource source);
}
