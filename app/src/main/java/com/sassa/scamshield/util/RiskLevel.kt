package com.sassa.scamshield.util

sealed class RiskLevel(val value: Int, val label: String, val color: Long) {
    object Safe : RiskLevel(0, "Safe", 0xFF22C55E)
    object ProbablySafe : RiskLevel(1, "Probably Safe", 0xFF84CC16)
    object LowRisk : RiskLevel(2, "Low Risk", 0xFFFFA940)
    object Suspicious : RiskLevel(3, "Suspicious", 0xFFFFA940)
    object HighRisk : RiskLevel(4, "High Risk", 0xFFF9320E)
    object KnownScam : RiskLevel(5, "Known Scam", 0xFFDC2626)

    companion object {
        val values: List<RiskLevel> = listOf(
            Safe, ProbablySafe, LowRisk, Suspicious, HighRisk, KnownScam
        )

        fun fromScore(score: Int): RiskLevel {
            return when (score) {
                in 0..24 -> Safe
                in 25..49 -> LowRisk
                in 50..74 -> Suspicious
                in 75..89 -> HighRisk
                in 90..100 -> KnownScam
                else -> Safe
            }
        }

        fun fromString(value: String): RiskLevel {
            return values.find { it.label.equals(value, ignoreCase = true) } ?: Safe
        }
    }

    fun colorValue(): androidx.compose.ui.graphics.Color {
        return androidx.compose.ui.graphics.Color(color)
    }
}
