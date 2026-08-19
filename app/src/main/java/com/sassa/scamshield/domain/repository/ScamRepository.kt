package com.sassa.scamshield.domain.repository

import com.sassa.scamshield.domain.model.MessageAnalysis
import com.sassa.scamshield.domain.model.ScamNumber
import com.sassa.scamshield.domain.model.ScamReport
import com.sassa.scamshield.domain.model.SecurityEvent
import kotlinx.coroutines.flow.Flow

interface ScamRepository {
    val scamNumbers: Flow<List<ScamNumber>>
    val scamReports: Flow<List<ScamReport>>
    val securityEvents: Flow<List<SecurityEvent>>
    val messageAnalyses: Flow<List<MessageAnalysis>>

    suspend fun getScamNumber(phoneNumber: String): ScamNumber?
    suspend fun saveScamNumber(number: ScamNumber)
    suspend fun saveScamReport(report: ScamReport)
    suspend fun saveMessageAnalysis(analysis: MessageAnalysis)
    suspend fun saveSecurityEvent(event: SecurityEvent)

    suspend fun getSecurityStats(): SecurityStats
    fun getSecurityStatsFlow(): kotlinx.coroutines.flow.Flow<SecurityStats>
    suspend fun countScamNumbers(): Int
    suspend fun countReports(): Int
    suspend fun clearAllData()
}

interface MessageRepository {
    suspend fun analyzeMessage(message: String, sender: String): MessageAnalysis
    fun getAllAnalyses(): kotlinx.coroutines.flow.Flow<List<com.sassa.scamshield.domain.model.MessageAnalysis>>
    suspend fun saveAnalysis(analysis: MessageAnalysis)
}

interface SettingsRepository {
    suspend fun getBoolean(key: String, default: Boolean): Boolean
    suspend fun setBoolean(key: String, value: Boolean)
    suspend fun getString(key: String, default: String): String
    suspend fun setString(key: String, value: String)
    suspend fun getInt(key: String, default: Int): Int
    suspend fun setInt(key: String, value: Int)
    suspend fun clearAll()
}

data class SecurityStats(
    val threatsBlocked: Int,
    val suspiciousMessages: Int,
    val suspiciousCalls: Int,
    val protectionConfidence: Int
)
