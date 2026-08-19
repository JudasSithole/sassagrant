package com.sassa.scamshield.domain.usecase

import com.sassa.scamshield.data.scanner.ScamDetectionEngine
import com.sassa.scamshield.domain.model.MessageAnalysis
import com.sassa.scamshield.domain.repository.MessageRepository
import com.sassa.scamshield.domain.repository.ScamRepository
import kotlinx.coroutines.flow.stateIn

class AnalyzeMessageUseCase(
    private val messageRepo: MessageRepository,
    private val scamRepo: ScamRepository
) {
    suspend operator fun invoke(message: String, sender: String = ""): MessageAnalysis {
        val analysis = messageRepo.analyzeMessage(message, sender)
        messageRepo.saveAnalysis(analysis)

        if (analysis.riskScore >= 75) {
            val event = com.sassa.scamshield.domain.model.SecurityEvent(
                eventType = com.sassa.scamshield.domain.model.EventType.MESSAGE_FLAGGED,
                title = "Suspicious Message Detected",
                description = analysis.messageContent.take(100),
                details = "Risk score: ${analysis.riskScore}",
                riskLevel = analysis.riskLevel,
                riskScore = analysis.riskScore
            )
            scamRepo.saveSecurityEvent(event)
        }

        return analysis
    }
}
