package com.sassa.scamshield.data.scanner

import com.sassa.scamshield.domain.model.DetectedIndicator
import com.sassa.scamshield.domain.model.IndicatorType
import com.sassa.scamshield.domain.model.MessageAnalysis
import com.sassa.scamshield.domain.model.RecommendedAction
import com.sassa.scamshield.util.RiskLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface ScamDetectionEngine {
    suspend fun analyzeMessage(
        message: String,
        sender: String = ""
    ): MessageAnalysis

    suspend fun analyzeNumber(
        phoneNumber: String
    ): NumberAnalysisResult
}

data class NumberAnalysisResult(
    val phoneNumber: String,
    val riskScore: Int,
    val riskLevel: RiskLevel,
    val reasons: List<String>,
    val isKnownScam: Boolean
)
