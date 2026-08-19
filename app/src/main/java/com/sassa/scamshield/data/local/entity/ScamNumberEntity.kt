package com.sassa.scamshield.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sassa.scamshield.util.ScamCategory

@Entity(tableName = "scam_numbers")
data class ScamNumberEntity(
    @PrimaryKey val id: String,
    val phoneNumber: String,
    val countryCode: String = "27",
    val riskScore: Int = 0,
    val reportCount: Int = 0,
    val category: ScamCategory = ScamCategory.OTHER,
    val firstReported: Long? = null,
    val lastReported: Long? = null,
    val isVerified: Boolean = false,
    val description: String = ""
)
