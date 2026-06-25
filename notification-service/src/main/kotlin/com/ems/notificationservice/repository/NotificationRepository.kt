package com.ems.notificationservice.repository

import com.ems.notificationservice.domain.Notification
import java.util.UUID
import org.springframework.data.mongodb.repository.MongoRepository

interface NotificationRepository : MongoRepository<Notification, UUID> {
    fun findAllByRecipientUserIdOrderByCreatedAtDesc(recipientUserId: UUID): List<Notification>
}
