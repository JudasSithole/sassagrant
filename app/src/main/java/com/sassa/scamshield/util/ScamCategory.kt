package com.sassa.scamshield.util

enum class ScamCategory(val displayName: String) {
    SASSA_SCAM("SASSA Scam"),
    BANKING_SCAM("Banking Scam"),
    PHISHING("Phishing"),
    IDENTITY_THEFT("Identity Theft Attempt"),
    FAKE_GOVERNMENT("Fake Government Official"),
    PRIZE_SCAM("Prize Scam"),
    COURIER_SCAM("Courier Scam"),
    OTHER("Other")
}
