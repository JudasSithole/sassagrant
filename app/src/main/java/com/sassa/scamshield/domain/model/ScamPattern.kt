package com.sassa.scamshield.domain.model

import java.time.Instant

data class ScamPattern(
    val id: String = "",
    val pattern: String = "",
    val description: String = "",
    val category: String = "",
    val weight: Int = 0,
    val isActive: Boolean = true,
    val firstSeen: Instant? = null,
    val lastSeen: Instant? = null
)
