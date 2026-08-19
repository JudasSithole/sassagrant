package com.sassa.scamshield.data.mapper

import com.sassa.scamshield.data.local.entity.MessageAnalysisEntity
import com.sassa.scamshield.data.local.entity.ScamNumberEntity
import com.sassa.scamshield.data.local.entity.ScamReportEntity
import com.sassa.scamshield.data.local.entity.SecurityEventEntity
import com.sassa.scamshield.domain.model.MessageAnalysis
import com.sassa.scamshield.domain.model.ScamNumber
import com.sassa.scamshield.domain.model.ScamReport
import com.sassa.scamshield.domain.model.SecurityEvent
import com.sassa.scamshield.domain.model.EventType
import java.time.Instant

fun ScamNumberEntity.toDomain(): ScamNumber {
    return ScamNumber(
        id = id,
        phoneNumber = phoneNumber,
        countryCode = countryCode,
        riskScore = riskScore,
        reportCount = reportCount,
        category = category,
        firstReported = firstReported?.let { Instant.ofEpochMilli(it) },
        lastReported = lastReported?.let { Instant.ofEpochMilli(it) },
        isVerified = isVerified,
        description = description
    )
}

fun ScamNumber.toEntity(): ScamNumberEntity {
    return ScamNumberEntity(
        id = id,
        phoneNumber = phoneNumber,
        countryCode = countryCode,
        riskScore = riskScore,
        reportCount = reportCount,
        category = category,
        firstReported = firstReported?.toEpochMilli(),
        lastReported = lastReported?.toEpochMilli(),
        isVerified = isVerified,
        description = description
    )
}

fun ScamReportEntity.toDomain(): ScamReport {
    return ScamReport(
        id = id,
        phoneNumber = phoneNumber,
        messageContent = messageContent,
        link = link,
        category = category,
        description = description,
        submittedAt = Instant.ofEpochMilli(submittedAt),
        isVerified = isVerified,
        userId = userId
    )
}

fun ScamReport.toEntity(): ScamReportEntity {
    return ScamReportEntity(
        id = id,
        phoneNumber = phoneNumber,
        messageContent = messageContent,
        link = link,
        category = category,
        description = description,
        submittedAt = submittedAt.toEpochMilli(),
        isVerified = isVerified,
        userId = userId
    )
}

fun MessageAnalysisEntity.toDomain(): MessageAnalysis {
    return MessageAnalysis(
        id = id,
        messageContent = messageContent,
        sender = sender,
        riskScore = riskScore,
        riskLevel = riskLevel,
        reasons = reasonsList(),
        detectedIndicators = indicatorsList(),
        recommendedAction = recommendedAction,
        analyzedAt = analyzedAt,
        isFromLocalRules = isFromLocalRules
    )
}

fun MessageAnalysis.toEntity(): MessageAnalysisEntity {
    return MessageAnalysisEntity(
        id = id,
        messageContent = messageContent,
        sender = sender,
        riskScore = riskScore,
        riskLevel = riskLevel,
        reasons = MessageAnalysisEntity.encodeReasons(reasons),
        detectedIndicatorsJson = MessageAnalysisEntity.encodeIndicators(detectedIndicators),
        recommendedAction = recommendedAction,
        analyzedAt = analyzedAt,
        isFromLocalRules = isFromLocalRules
    )
}

fun SecurityEventEntity.toDomain(): SecurityEvent {
    return SecurityEvent(
        id = id,
        eventType = EventType.valueOf(eventType),
        title = title,
        description = description,
        details = details,
        riskLevel = riskLevel,
        riskScore = riskScore,
        timestamp = Instant.ofEpochMilli(timestamp),
        phoneNumber = phoneNumber,
        messageContent = messageContent,
        isResolved = isResolved,
        relatedReportId = relatedReportId
    )
}

fun SecurityEvent.toEntity(): SecurityEventEntity {
    return SecurityEventEntity(
        id = id,
        eventType = eventType.name,
        title = title,
        description = description,
        details = details,
        riskLevel = riskLevel,
        riskScore = riskScore,
        timestamp = timestamp.toEpochMilli(),
        phoneNumber = phoneNumber,
        messageContent = messageContent,
        isResolved = isResolved,
        relatedReportId = relatedReportId
    )
}
