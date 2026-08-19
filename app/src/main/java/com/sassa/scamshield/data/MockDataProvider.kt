package com.sassa.scamshield.data

import com.sassa.scamshield.data.local.dao.MessageAnalysisDao
import com.sassa.scamshield.data.local.dao.ScamNumberDao
import com.sassa.scamshield.data.local.dao.ScamReportDao
import com.sassa.scamshield.data.local.dao.SecurityEventDao
import com.sassa.scamshield.data.mapper.toEntity
import com.sassa.scamshield.data.scanner.DetectionConfig
import com.sassa.scamshield.data.scanner.RuleBasedScamDetector
import com.sassa.scamshield.domain.model.MessageAnalysis
import com.sassa.scamshield.domain.model.ScamNumber
import com.sassa.scamshield.domain.model.SecurityEvent
import com.sassa.scamshield.domain.model.EventType
import com.sassa.scamshield.util.RiskLevel
import com.sassa.scamshield.util.ScamCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

class MockDataProvider(
    private val scamNumberDao: ScamNumberDao,
    private val scamReportDao: ScamReportDao,
    private val messageAnalysisDao: MessageAnalysisDao,
    private val securityEventDao: SecurityEventDao
) {

    fun seedIfEmpty() {
        CoroutineScope(Dispatchers.IO).launch {
            if (scamNumberDao.count() > 0) return@launch

            seedScamNumbers()
            seedMessageAnalyses()
            seedSecurityEvents()
            seedReports()
        }
    }

    private suspend fun seedScamNumbers() {
        val numbers = listOf(
            ScamNumber(
                id = UUID.randomUUID().toString(),
                phoneNumber = "+27791234567",
                countryCode = "27",
                riskScore = 95,
                reportCount = 42,
                category = ScamCategory.SASSA_SCAM,
                firstReported = Instant.parse("2024-01-15T10:00:00Z"),
                lastReported = Instant.parse("2025-06-20T14:30:00Z"),
                isVerified = true,
                description = "Frequently reported SASSA payment suspension scam"
            ),
            ScamNumber(
                id = UUID.randomUUID().toString(),
                phoneNumber = "+27825551234",
                countryCode = "27",
                riskScore = 78,
                reportCount = 15,
                category = ScamCategory.BANKING_SCAM,
                firstReported = Instant.parse("2024-03-10T09:00:00Z"),
                lastReported = Instant.parse("2025-05-20T12:00:00Z"),
                isVerified = false,
                description = "Impersonates bank agent requesting OTP"
            ),
            ScamNumber(
                id = UUID.randomUUID().toString(),
                phoneNumber = "+27679876543",
                countryCode = "27",
                riskScore = 85,
                reportCount = 23,
                category = ScamCategory.PRIZE_SCAM,
                firstReported = Instant.parse("2024-04-05T11:00:00Z"),
                lastReported = Instant.parse("2025-06-15T16:45:00Z"),
                isVerified = true,
                description = "Lottery/prize scam targeting SASSA beneficiaries"
            )
        )
        numbers.forEach { scamNumberDao.insert(it.toEntity()) }
    }

    private suspend fun seedMessageAnalyses() {
        val detector = RuleBasedScamDetector(DetectionConfig())
        val messages = listOf(
            Pair(
                "SASSA: Your grant payment has been suspended. Verify your account immediately using the link below.",
                "SASSA Alerts"
            ),
            Pair(
                "Your SASSA grant will be cancelled within 24 hours. Click here to update your banking details: suspicious-link.example",
                "SASSA Grants"
            ),
            Pair(
                "URGENT: SASSA needs your OTP to verify your account. Please send your verification code immediately.",
                "+27791234567"
            ),
            Pair(
                "Hello, how are you today?",
                "Friend"
            ),
            Pair(
                "Your SASSA payment of R350 will be processed this week.",
                "SASSA Official"
            ),
            Pair(
                "Reminder: Your SASSA appointment is scheduled for tomorrow at 10 AM.",
                "SASSA"
            ),
            Pair(
                "Congratulations! You have won R5000 in our SASSA beneficiary draw. Claim now with your ID number.",
                "SA Lottery"
            ),
            Pair(
                "Dear beneficiary, your monthly grant payment is ready. No action required.",
                "SASSA"
            ),
            Pair(
                "SASSA: Enter your PIN and ID number to receive your payment.",
                "SASSA Support"
            ),
            Pair(
                "Hi mom, can you pick up some milk on your way home?",
                "Sarah"
            ),
            Pair(
                "Your SASSA status has been updated. Your new payment amount is R450. Click to view details.",
                "SASSA Gov"
            ),
            Pair(
                "Don't forget your SASSA biometric verification appointment tomorrow.",
                "SASSA"
            ),
            Pair(
                "SASSA: Your account will be closed. Send your ID and OTP to verify.",
                "SASSA Help"
            ),
            Pair(
                "Your SASSA balance is R0. Please deposit money into your account to continue receiving grants.",
                "SASSA"
            ),
            Pair(
                "Meeting at 3pm today?",
                "Boss"
            ),
            Pair(
                "URGENT: Your SASSA card has been blocked. Call 0800-123-456 to unblock immediately.",
                "SASSA Security"
            )
        )

        messages.forEach { (content, sender) ->
            val analysis = detector.analyzeMessage(content, sender)
            messageAnalysisDao.insert(analysis.toEntity())
        }
    }

    private suspend fun seedSecurityEvents() {
        val now = System.currentTimeMillis()
        val events = listOf(
            SecurityEvent(
                id = UUID.randomUUID().toString(),
                eventType = EventType.MESSAGE_FLAGGED,
                title = "Suspicious Message Detected",
                description = "Your grant payment is ready. Click this link...",
                details = "Risk score: 92",
                riskLevel = RiskLevel.HighRisk,
                riskScore = 92,
                timestamp = Instant.ofEpochMilli(now - 120000),
            ),
            SecurityEvent(
                id = UUID.randomUUID().toString(),
                eventType = EventType.CALL_BLOCKED,
                title = "Suspicious Number Blocked",
                description = "+27 79 123 4567 was automatically blocked",
                details = "Known SASSA scam number",
                riskLevel = RiskLevel.KnownScam,
                riskScore = 95,
                timestamp = Instant.ofEpochMilli(now - 3600000),
                phoneNumber = "+27791234567"
            ),
            SecurityEvent(
                id = UUID.randomUUID().toString(),
                eventType = EventType.MESSAGE_DETECTED,
                title = "SASSA message detected",
                description = "SASSA: Your grant payment has been suspended...",
                details = "Flagged by local rules",
                riskLevel = RiskLevel.Suspicious,
                riskScore = 78,
                timestamp = Instant.ofEpochMilli(now - 86400000),
            ),
            SecurityEvent(
                id = UUID.randomUUID().toString(),
                eventType = EventType.MESSAGE_FLAGGED,
                title = "Suspicious Message Detected",
                description = "Congratulations! You have won R5000 in our draw...",
                details = "Prize scam indicator detected",
                riskLevel = RiskLevel.HighRisk,
                riskScore = 85,
                timestamp = Instant.ofEpochMilli(now - 172800000),
            ),
            SecurityEvent(
                id = UUID.randomUUID().toString(),
                eventType = EventType.MESSAGE_DETECTED,
                title = "Message marked safe",
                description = "Your SASSA payment of R350 will be processed",
                details = "Scanned as safe",
                riskLevel = RiskLevel.Safe,
                riskScore = 10,
                timestamp = Instant.ofEpochMilli(now - 259200000),
            )
        )
        events.forEach { securityEventDao.insert(it.toEntity()) }
    }

    private suspend fun seedReports() {
        val report = com.sassa.scamshield.domain.model.ScamReport(
            id = UUID.randomUUID().toString(),
            phoneNumber = "+27791234567",
            messageContent = "SASSA: Your grant payment has been suspended...",
            category = ScamCategory.SASSA_SCAM,
            description = "Fake payment suspension message",
            submittedAt = Instant.now(),
            isVerified = false
        )
        scamReportDao.insert(report.toEntity())
    }
}
