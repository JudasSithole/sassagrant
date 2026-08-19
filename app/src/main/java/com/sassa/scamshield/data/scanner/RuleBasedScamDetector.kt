package com.sassa.scamshield.data.scanner

class RuleBasedScamDetector(
    private val config: DetectionConfig = DetectionConfig()
) : ScamDetectionEngine {

    override suspend fun analyzeMessage(
        message: String,
        sender: String
    ): com.sassa.scamshield.domain.model.MessageAnalysis {
        val indicators = mutableListOf<com.sassa.scamshield.domain.model.DetectedIndicator>()
        var totalScore = 0
        val reasons = mutableListOf<String>()

        for (rule in config.rules) {
            if (rule.pattern.containsMatchIn(message)) {
                totalScore += rule.weight
                indicators.add(
                    com.sassa.scamshield.domain.model.DetectedIndicator(
                        type = rule.indicatorType,
                        description = rule.description,
                        weight = rule.weight
                    )
                )
                reasons.add(rule.description)
            }
        }

        if (sender.isNotBlank()) {
            val senderMatch = isSenderSuspicious(sender)
            if (senderMatch != null) {
                totalScore += senderMatch.indicator.weight
                indicators.add(senderMatch.indicator)
                reasons.add(senderMatch.reason)
            }
        }

        val clampedScore = totalScore.coerceIn(0, 100)
        val riskLevel = com.sassa.scamshield.util.RiskLevel.fromScore(clampedScore)
        val recommendedAction = determineRecommendedAction(riskLevel, indicators)

        return com.sassa.scamshield.domain.model.MessageAnalysis(
            messageContent = message,
            sender = sender,
            riskScore = clampedScore,
            riskLevel = riskLevel,
            reasons = reasons,
            detectedIndicators = indicators,
            recommendedAction = recommendedAction,
            analyzedAt = System.currentTimeMillis(),
            isFromLocalRules = true
        )
    }

    override suspend fun analyzeNumber(phoneNumber: String): NumberAnalysisResult {
        val clean = phoneNumber.replace("+", "").replace(" ", "")
        var score = 0
        val reasons = mutableListOf<String>()
        val isKnownScam = false

        if (clean.length < 9 || clean.length > 15) {
            score += 10
            reasons.add("Invalid phone number format")
        }

        val knownPrefixes = listOf("0800", "086", "087")
        if (knownPrefixes.any { clean.startsWith(it) }) {
            score += 5
            reasons.add("Uses toll-free or non-geographic prefix")
        }

        val riskLevel = com.sassa.scamshield.util.RiskLevel.fromScore(score.coerceIn(0, 100))
        return NumberAnalysisResult(
            phoneNumber = phoneNumber,
            riskScore = score.coerceIn(0, 100),
            riskLevel = riskLevel,
            reasons = reasons,
            isKnownScam = isKnownScam
        )
    }

    private fun isSenderSuspicious(sender: String): SenderCheckResult? {
        val lowerSender = sender.lowercase()

        val smsPatterns = listOf(
            "sassa" to 20,
            "social" to 15
        )

        for ((pattern, weight) in smsPatterns) {
            if (lowerSender.contains(pattern) && !lowerSender.contains("gov") && !lowerSender.contains("gov.za")) {
                return SenderCheckResult(
                    indicator = com.sassa.scamshield.domain.model.DetectedIndicator(
                        type = com.sassa.scamshield.domain.model.IndicatorType.KNOWN_SCAM_SENDER,
                        description = "Sender mimics official SASSA branding",
                        weight = weight
                    ),
                    reason = "Sender uses unofficial branding that mimics SASSA"
                )
            }
        }

        return null
    }

    private fun determineRecommendedAction(
        riskLevel: com.sassa.scamshield.util.RiskLevel,
        indicators: List<com.sassa.scamshield.domain.model.DetectedIndicator>
    ): com.sassa.scamshield.domain.model.RecommendedAction {
        return when (riskLevel) {
            com.sassa.scamshield.util.RiskLevel.KnownScam,             com.sassa.scamshield.util.RiskLevel.HighRisk -> com.sassa.scamshield.domain.model.RecommendedAction.DO_NOT_CLICK
            com.sassa.scamshield.util.RiskLevel.Suspicious -> com.sassa.scamshield.domain.model.RecommendedAction.DO_NOT_RESPOND
            com.sassa.scamshield.util.RiskLevel.LowRisk -> com.sassa.scamshield.domain.model.RecommendedAction.NONE
            com.sassa.scamshield.util.RiskLevel.ProbablySafe -> com.sassa.scamshield.domain.model.RecommendedAction.NONE
            com.sassa.scamshield.util.RiskLevel.Safe -> com.sassa.scamshield.domain.model.RecommendedAction.NONE
        }
    }

    private data class SenderCheckResult(
        val indicator: com.sassa.scamshield.domain.model.DetectedIndicator,
        val reason: String
    )
}
