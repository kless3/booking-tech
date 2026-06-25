package com.ems.notificationservice.domain

import java.time.LocalDateTime
import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("processed_kafka_messages")
class ProcessedKafkaMessage(
    @Id
    val messageId: UUID,

    @Indexed
    val topic: String,

    val messageKey: String,

    val processedAt: LocalDateTime,
)
