package com.sassa.scamshield.domain.model

import com.sassa.scamshield.util.ScamCategory
import com.sassa.scamshield.util.RiskLevel
import java.time.Instant

data class ScamNumber(
    val id: String = "",
    val phoneNumber: String = "",
    val countryCode: String = "27",
    val riskScore: Int = 0,
    val reportCount: Int = 0,
    val category: ScamCategory = ScamCategory.OTHER,
    val firstReported: Instant? = null,
    val lastReported: Instant? = null,
    val isVerified: Boolean = false,
    val description: String = ""
) {
    fun riskLevel(): RiskLevel = RiskLevel.fromScore(riskScore)

    fun formattedNumber(): String {
        val clean = phoneNumber.replace("+", "").replace(" ", "")
        return if (clean.startsWith("27")) {
            "0" + clean.substring(2)
        } else {
            clean
        }
    }

    fun displayNumber(): String {
        val clean = phoneNumber.replace("+", "").replace(" ", "")
        val local = if (clean.startsWith("27")) {
            "0" + clean.substring(2)
        } else {
            clean
        }
        return if (local.length >= 10) {
            "${local.substring(0, 3)} ${local.substring(3, 6)} ${local.substring(6)}"
        } else {
            local
        }
    }
}
