package com.sassa.scamshield.util

object Constants {
    const val DATABASE_NAME = "scamshield_db"
    const val SHARED_PREFS_NAME = "scamshield_prefs"
    const val PREFS_KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    const val PREFS_KEY_MESSAGE_PROTECTION_ENABLED = "message_protection_enabled"
    const val PREFS_KEY_CALL_PROTECTION_ENABLED = "call_protection_enabled"
    const val PREFS_KEY_AUTO_ALERTS_ENABLED = "auto_alerts_enabled"
    const val PREFS_KEY_BLOCK_HIGH_RISK_CALLS = "block_high_risk_calls"

    // Country prefix for South Africa
    const val SA_COUNTRY_CODE = "27"
    const val SA_FULL_PREFIX = "+27"

    // Minimum number of reports for a number to be considered "reported by multiple users"
    const val MIN_REPORTS_FOR_MULTIPLE = 5
}
