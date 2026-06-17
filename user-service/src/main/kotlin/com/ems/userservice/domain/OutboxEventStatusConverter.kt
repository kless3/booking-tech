package com.ems.userservice.domain

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class OutboxEventStatusConverter : AttributeConverter<OutboxEventStatus, String> {
    override fun convertToDatabaseColumn(attribute: OutboxEventStatus?): String? = attribute?.name

    override fun convertToEntityAttribute(dbData: String?): OutboxEventStatus? =
        dbData?.let(OutboxEventStatus::valueOf)
}
