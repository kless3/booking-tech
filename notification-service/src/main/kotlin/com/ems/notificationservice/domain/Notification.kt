package com.ems.notificationservice.domain

import java.time.LocalDateTime
import java.util.UUID
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("notifications")
@CompoundIndex(name = "idx_notifications_recipient_created_at", def = "{'recipientUserId': 1, 'createdAt': -1}")
@CompoundIndex(name = "idx_notifications_status_created_at", def = "{'status': 1, 'createdAt': -1}")
class Notification(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Indexed
    val recipientUserId: UUID?,

    val channel: NotificationChannel,

    @Indexed
    var status: NotificationStatus = NotificationStatus.PENDING,

    @Indexed
    val sourceEventId: UUID,

    val sourceEventType: String,

    val subject: String,

    val body: String,

    var failureReason: String? = null,

    var deliveryAttempts: Int = 0,

    @CreatedDate
    var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,

    var sentAt: LocalDateTime? = null,
) {
    fun markDeliveryAttempt(attempt: Int) {
        deliveryAttempts = attempt
        status = NotificationStatus.RETRYING
    }

    fun markSent(sentAt: LocalDateTime) {
        status = NotificationStatus.SENT
        failureReason = null
        this.sentAt = sentAt
    }

    fun markFailed(reason: String) {
        status = NotificationStatus.FAILED
        failureReason = reason.take(MAX_FAILURE_REASON_LENGTH)
    }

    private companion object {
        const val MAX_FAILURE_REASON_LENGTH = 1024
    }
}
