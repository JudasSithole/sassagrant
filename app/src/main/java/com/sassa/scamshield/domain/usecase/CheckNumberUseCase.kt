package com.sassa.scamshield.domain.usecase

import com.sassa.scamshield.data.scanner.NumberAnalysisResult
import com.sassa.scamshield.data.scanner.ScamDetectionEngine
import com.sassa.scamshield.domain.model.ScamNumber
import com.sassa.scamshield.domain.model.ScamReport
import com.sassa.scamshield.domain.model.SecurityEvent
import com.sassa.scamshield.domain.model.EventType
import com.sassa.scamshield.domain.repository.ScamRepository
import com.sassa.scamshield.util.RiskLevel
import com.sassa.scamshield.util.ScamCategory

class CheckNumberUseCase(
    private val scamRepo: ScamRepository,
    private val engine: ScamDetectionEngine
) {
    suspend operator fun invoke(phoneNumber: String): NumberLookupResult {
        val knownNumber = scamRepo.getScamNumber(phoneNumber)
        val localResult = engine.analyzeNumber(phoneNumber)

        val combinedScore = if (knownNumber != null) {
            maxOf(localResult.riskScore, knownNumber.riskScore)
        } else {
            localResult.riskScore
        }

        val riskLevel = RiskLevel.fromScore(combinedScore)
        val isKnownScam = knownNumber != null && knownNumber.riskScore >= 90

        val reasons = buildList {
            if (knownNumber != null) {
                add("Reported ${knownNumber.reportCount} times")
                if (knownNumber.category != ScamCategory.OTHER) {
                    add("Category: ${knownNumber.category.displayName}")
                }
                if (knownNumber.isVerified) {
                    add("Verified scam number")
                } else {
                    add("Unverified report")
                }
            }
            addAll(localResult.reasons)
        }

        return NumberLookupResult(
            phoneNumber = phoneNumber,
            riskScore = combinedScore,
            riskLevel = riskLevel,
            reasons = reasons,
            isKnownScam = isKnownScam,
            knownNumber = knownNumber
        )
    }

    suspend fun reportNumber(
        phoneNumber: String,
        category: ScamCategory,
        description: String
    ): ScamReport {
        val report = ScamReport(
            phoneNumber = phoneNumber,
            category = category,
            description = description
        )
        scamRepo.saveScamReport(report)

        val numberEntity = ScamNumber(
            phoneNumber = phoneNumber,
            riskScore = RiskLevel.HighRisk.value,
            reportCount = 1,
            category = category,
            firstReported = java.time.Instant.now(),
            lastReported = java.time.Instant.now(),
            isVerified = false,
            description = description
        )
        scamRepo.saveScamNumber(numberEntity)

        val event = SecurityEvent(
            eventType = EventType.REPORT_RECEIVED,
            title = "New Scam Report",
            description = "Number reported: ${phoneNumber.take(10)}...",
            details = "Category: ${category.displayName}",
            riskLevel = RiskLevel.HighRisk,
            riskScore = 75
        )
        scamRepo.saveSecurityEvent(event)

        return report
    }

    data class NumberLookupResult(
        val phoneNumber: String,
        val riskScore: Int,
        val riskLevel: RiskLevel,
        val reasons: List<String>,
        val isKnownScam: Boolean,
        val knownNumber: ScamNumber?
    )
}
