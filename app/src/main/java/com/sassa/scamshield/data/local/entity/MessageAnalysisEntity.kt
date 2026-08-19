package com.sassa.scamshield.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sassa.scamshield.domain.model.DetectedIndicator
import com.sassa.scamshield.domain.model.RecommendedAction
import com.sassa.scamshield.util.RiskLevel
import kotlinx.serialization.json.Json

@Entity(tableName = "message_analyses")
data class MessageAnalysisEntity(
    @PrimaryKey val id: String,
    val messageContent: String,
    val sender: String = "",
    val riskScore: Int = 0,
    val riskLevel: RiskLevel = RiskLevel.Safe,
    val reasons: String = "[]",
    val detectedIndicatorsJson: String = "[]",
    val recommendedAction: RecommendedAction = RecommendedAction.NONE,
    val analyzedAt: Long = System.currentTimeMillis(),
    val isFromLocalRules: Boolean = true
) {
    fun reasonsList(): List<String> {
        return if (reasons.isBlank()) emptyList() else {
            Json.decodeFromString<List<String>>(reasons)
        }
    }

    fun indicatorsList(): List<DetectedIndicator> {
        return if (detectedIndicatorsJson.isBlank()) emptyList() else {
            Json.decodeFromString<List<DetectedIndicator>>(detectedIndicatorsJson)
        }
    }

    companion object {
        fun encodeReasons(reasons: List<String>): String = Json.encodeToString(reasons)
        fun encodeIndicators(indicators: List<DetectedIndicator>): String =
            Json.encodeToString(indicators)
    }
}
