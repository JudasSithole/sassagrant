package com.sassa.scamshield.domain.model

import com.sassa.scamshield.util.RiskLevel

data class MessageAnalysis(
    val id: String = "",
    val messageContent: String,
    val sender: String = "",
    val riskScore: Int = 0,
    val riskLevel: RiskLevel = RiskLevel.Safe,
    val reasons: List<String> = emptyList(),
    val detectedIndicators: List<DetectedIndicator> = emptyList(),
    val recommendedAction: RecommendedAction = RecommendedAction.NONE,
    val analyzedAt: Long = System.currentTimeMillis(),
    val isFromLocalRules: Boolean = true
)

enum class RecommendedAction {
    NONE,
    DO_NOT_CLICK,
    DO_NOT_RESPOND,
    BLOCK_SENDER,
    REPORT_SCAM
}

data class DetectedIndicator(
    val type: IndicatorType,
    val description: String,
    val weight: Int
)

enum class IndicatorType {
    KEYWORD,
    URL,
    URGENT_LANGUAGE,
    OTP_REQUEST,
    PIN_REQUEST,
    BANKING_DETAILS,
    ACCOUNT_SUSPENSION,
    PERSONAL_INFO_REQUEST,
    EXTERNAL_LINK,
    SUSPICIOUS_DOMAIN,
    SHORTENED_URL,
    KNOWN_SCAM_SENDER,
    KNOWN_SCAM_PATTERN,
    PRIZE_CLAIM,
    PAYMENT_INSTRUCTION,
    FAKE_GRANT,
    FAKE_APPLICATION
}
