package com.sassa.scamshield.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sassa.scamshield.util.RiskLevel

@Entity(tableName = "security_events")
data class SecurityEventEntity(
    @PrimaryKey val id: String,
    val eventType: String,
    val title: String,
    val description: String,
    val details: String,
    val riskLevel: RiskLevel,
    val riskScore: Int,
    val timestamp: Long,
    val phoneNumber: String? = null,
    val messageContent: String? = null,
    val isResolved: Boolean = false,
    val relatedReportId: String? = null
)
