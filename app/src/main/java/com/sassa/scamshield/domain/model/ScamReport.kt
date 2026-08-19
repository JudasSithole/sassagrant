package com.sassa.scamshield.domain.model

import com.sassa.scamshield.util.ScamCategory
import java.time.Instant

data class ScamReport(
    val id: String = "",
    val phoneNumber: String? = null,
    val messageContent: String? = null,
    val link: String? = null,
    val category: ScamCategory = ScamCategory.OTHER,
    val description: String = "",
    val submittedAt: Instant = Instant.now(),
    val isVerified: Boolean = false,
    val userId: String = ""
)
