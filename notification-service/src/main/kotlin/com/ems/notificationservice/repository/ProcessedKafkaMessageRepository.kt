package com.ems.notificationservice.repository

import com.ems.notificationservice.domain.ProcessedKafkaMessage
import java.util.UUID
import org.springframework.data.mongodb.repository.MongoRepository

interface ProcessedKafkaMessageRepository : MongoRepository<ProcessedKafkaMessage, UUID>
