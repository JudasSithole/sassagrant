package com.sassa.scamshield.domain.usecase

import com.sassa.scamshield.domain.model.ScamReport
import com.sassa.scamshield.domain.model.SecurityEvent
import com.sassa.scamshield.domain.model.EventType
import com.sassa.scamshield.domain.repository.ScamRepository
import com.sassa.scamshield.util.RiskLevel
import com.sassa.scamshield.util.ScamCategory

class ReportScamUseCase(
    private val scamRepo: ScamRepository
) {
    suspend operator fun invoke(
        phoneNumber: String? = null,
        messageContent: String? = null,
        link: String? = null,
        category: ScamCategory,
        description: String
    ): ScamReport {
        val report = ScamReport(
            phoneNumber = phoneNumber,
            messageContent = messageContent,
            link = link,
            category = category,
            description = description,
            isVerified = false
        )
        scamRepo.saveScamReport(report)

        if (phoneNumber != null) {
            val number = com.sassa.scamshield.domain.model.ScamNumber(
                phoneNumber = phoneNumber,
                riskScore = RiskLevel.fromScore(75).value,
                reportCount = 1,
                category = category,
                firstReported = java.time.Instant.now(),
                lastReported = java.time.Instant.now()
            )
            scamRepo.saveScamNumber(number)
        }

        val event = SecurityEvent(
            eventType = EventType.REPORT_RECEIVED,
            title = "New Scam Report Received",
            description = "Report submitted for review",
            details = "Category: ${category.displayName}",
            riskLevel = RiskLevel.Suspicious,
            riskScore = 50
        )
        scamRepo.saveSecurityEvent(event)

        return report
    }
}
