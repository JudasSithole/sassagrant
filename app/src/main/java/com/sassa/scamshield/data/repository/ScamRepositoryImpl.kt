package com.sassa.scamshield.data.repository

import com.sassa.scamshield.data.local.dao.MessageAnalysisDao
import com.sassa.scamshield.data.local.dao.ScamNumberDao
import com.sassa.scamshield.data.local.dao.ScamReportDao
import com.sassa.scamshield.data.local.dao.SecurityEventDao
import com.sassa.scamshield.data.mapper.toDomain
import com.sassa.scamshield.data.mapper.toEntity
import com.sassa.scamshield.domain.model.MessageAnalysis
import com.sassa.scamshield.domain.model.ScamNumber
import com.sassa.scamshield.domain.model.ScamReport
import com.sassa.scamshield.domain.model.SecurityEvent
import com.sassa.scamshield.domain.model.EventType
import com.sassa.scamshield.domain.repository.ScamRepository
import com.sassa.scamshield.domain.repository.SecurityStats
import com.sassa.scamshield.util.RiskLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class ScamRepositoryImpl(
    private val scamNumberDao: ScamNumberDao,
    private val scamReportDao: ScamReportDao,
    private val messageAnalysisDao: MessageAnalysisDao,
    private val securityEventDao: SecurityEventDao
) : ScamRepository {

    override val scamNumbers: Flow<List<ScamNumber>> =
        scamNumberDao.getAllNumbers().map { it.map { entity -> entity.toDomain() } }

    override val scamReports: Flow<List<ScamReport>> =
        scamReportDao.getAllReports().map { it.map { entity -> entity.toDomain() } }

    override val securityEvents: Flow<List<SecurityEvent>> =
        securityEventDao.getAllEvents().map { it.map { entity -> entity.toDomain() } }

    override val messageAnalyses: Flow<List<MessageAnalysis>> =
        messageAnalysisDao.getAll().map { it.map { entity -> entity.toDomain() } }

    override suspend fun getScamNumber(phoneNumber: String): ScamNumber? {
        return scamNumberDao.getNumber(phoneNumber)?.toDomain()
    }

    override suspend fun saveScamNumber(number: ScamNumber) {
        val entity = if (number.id.isBlank()) {
            number.copy(id = UUID.randomUUID().toString()).toEntity()
        } else {
            number.toEntity()
        }
        val existing = scamNumberDao.getNumber(number.phoneNumber)
        if (existing != null) {
            scamNumberDao.update(entity.copy(id = existing.id))
        } else {
            scamNumberDao.insert(entity)
        }
    }

    override suspend fun saveScamReport(report: ScamReport) {
        val entity = if (report.id.isBlank()) {
            report.copy(id = UUID.randomUUID().toString()).toEntity()
        } else {
            report.toEntity()
        }
        scamReportDao.insert(entity)
    }

    override suspend fun saveMessageAnalysis(analysis: MessageAnalysis) {
        val entity = if (analysis.id.isBlank()) {
            analysis.copy(id = UUID.randomUUID().toString()).toEntity()
        } else {
            analysis.toEntity()
        }
        messageAnalysisDao.insert(entity)
    }

    override suspend fun saveSecurityEvent(event: SecurityEvent) {
        val entity = if (event.id.isBlank()) {
            event.copy(id = UUID.randomUUID().toString()).toEntity()
        } else {
            event.toEntity()
        }
        securityEventDao.insert(entity)
    }

    override suspend fun getSecurityStats(): SecurityStats {
        val threats = securityEventDao.countThreatsBlocked()
        val suspiciousMessages = securityEventDao.countSuspiciousMessages()
        val suspiciousCalls = securityEventDao.countSuspiciousCalls()
        val confidence = calculateConfidence(threats, suspiciousMessages, suspiciousCalls)
        return SecurityStats(
            threatsBlocked = threats,
            suspiciousMessages = suspiciousMessages,
            suspiciousCalls = suspiciousCalls,
            protectionConfidence = confidence
        )
    }

    override fun getSecurityStatsFlow(): kotlinx.coroutines.flow.Flow<SecurityStats> {
        return securityEventDao.getAllEvents().map { events ->
            val threats = events.count {
                it.riskLevel == com.sassa.scamshield.util.RiskLevel.HighRisk ||
                    it.riskLevel == com.sassa.scamshield.util.RiskLevel.KnownScam
            }
            val suspiciousMessages = events.count {
                it.eventType == EventType.MESSAGE_DETECTED.name ||
                it.eventType == EventType.MESSAGE_FLAGGED.name
            }
            val suspiciousCalls = events.count {
                it.eventType == EventType.CALL_FLAGGED.name ||
                it.eventType == EventType.CALL_BLOCKED.name
            }
            val total = threats + suspiciousMessages + suspiciousCalls
            val confidence = if (total == 0) 95 else (100 - (threats.toFloat() / maxOf(total.toFloat(), 1f) * 5f)).toInt().coerceIn(80, 99)
            SecurityStats(threats, suspiciousMessages, suspiciousCalls, confidence)
        }
    }

    private fun calculateConfidence(
        threats: Int,
        suspiciousMessages: Int,
        suspiciousCalls: Int
    ): Int {
        val total = threats + suspiciousMessages + suspiciousCalls
        return if (total == 0) 95 else (100 - (threats.toFloat() / maxOf(total.toFloat(), 1f) * 5f)).toInt().coerceIn(80, 99)
    }

    override suspend fun countScamNumbers(): Int = scamNumberDao.count()

    override suspend fun countReports(): Int = 0

    override suspend fun clearAllData() {
        scamNumberDao.clearAll()
        scamReportDao.clearAll()
        messageAnalysisDao.clearAll()
        securityEventDao.clearAll()
    }
}
