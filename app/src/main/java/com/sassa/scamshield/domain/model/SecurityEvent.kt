package com.sassa.scamshield.domain.model

import com.sassa.scamshield.util.RiskLevel
import java.time.Instant

data class SecurityEvent(
    val id: String = "",
    val eventType: EventType,
    val title: String,
    val description: String,
    val details: String = "",
    val riskLevel: RiskLevel = RiskLevel.Safe,
    val riskScore: Int = 0,
    val timestamp: Instant = Instant.now(),
    val phoneNumber: String? = null,
    val messageContent: String? = null,
    val isResolved: Boolean = false,
    val relatedReportId: String? = null
)

enum class EventType {
    MESSAGE_DETECTED,
    NUMBER_BLOCKED,
    MESSAGE_MARKED_SAFE,
    REPORT_RECEIVED,
    CALL_FLAGGED,
    CALL_BLOCKED,
    MESSAGE_FLAGGED,
    SYSTEM_SCAN_COMPLETE
}
