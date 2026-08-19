package com.sassa.scamshield.data.repository

import com.sassa.scamshield.data.local.dao.MessageAnalysisDao
import com.sassa.scamshield.data.mapper.toDomain
import com.sassa.scamshield.data.mapper.toEntity
import com.sassa.scamshield.data.scanner.ScamDetectionEngine
import com.sassa.scamshield.domain.model.MessageAnalysis
import com.sassa.scamshield.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageRepositoryImpl(
    private val engine: ScamDetectionEngine,
    private val messageAnalysisDao: MessageAnalysisDao
) : MessageRepository {

    override suspend fun analyzeMessage(message: String, sender: String): MessageAnalysis {
        return engine.analyzeMessage(message, sender)
    }

    override fun getAllAnalyses(): Flow<List<MessageAnalysis>> =
        messageAnalysisDao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun saveAnalysis(analysis: MessageAnalysis) {
        messageAnalysisDao.insert(analysis.toEntity())
    }
}
