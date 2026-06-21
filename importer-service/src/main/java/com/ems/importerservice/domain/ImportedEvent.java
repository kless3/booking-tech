package com.ems.importerservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "imported_events")
public class ImportedEvent {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32, updatable = false)
    private EventSource source;

    @Column(name = "external_id", nullable = false, updatable = false)
    private String externalId;

    @Column(name = "event_id")
    private UUID eventId;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ImportStatus status;

    @Column(name = "failure_reason", length = 1024)
    private String failureReason;

    @Column(name = "imported_at")
    private LocalDateTime importedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected ImportedEvent() {
    }

    public ImportedEvent(EventSource source, String externalId, String title) {
        this.source = source;
        this.externalId = externalId;
        this.title = title;
        this.status = ImportStatus.FAILED;
    }

    public void markImported(UUID eventId, LocalDateTime importedAt) {
        this.eventId = eventId;
        this.status = ImportStatus.IMPORTED;
        this.failureReason = null;
        this.importedAt = importedAt;
    }

    public void markFailed(String reason) {
        this.status = ImportStatus.FAILED;
        this.failureReason = reason.length() > 1024 ? reason.substring(0, 1024) : reason;
    }

    public UUID getId() {
        return id;
    }

    public EventSource getSource() {
        return source;
    }

    public String getExternalId() {
        return externalId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public ImportStatus getStatus() {
        return status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public LocalDateTime getImportedAt() {
        return importedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
