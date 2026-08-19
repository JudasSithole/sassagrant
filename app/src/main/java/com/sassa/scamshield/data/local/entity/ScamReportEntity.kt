package com.sassa.scamshield.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sassa.scamshield.util.ScamCategory

@Entity(tableName = "scam_reports")
data class ScamReportEntity(
    @PrimaryKey val id: String,
    val phoneNumber: String? = null,
    val messageContent: String? = null,
    val link: String? = null,
    val category: ScamCategory = ScamCategory.OTHER,
    val description: String = "",
    val submittedAt: Long = System.currentTimeMillis(),
    val isVerified: Boolean = false,
    val userId: String = ""
)
