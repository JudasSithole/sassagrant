package com.sassa.scamshield.data.scanner

import com.sassa.scamshield.domain.model.IndicatorType

data class DetectionConfig(
    val rules: List<DetectionRule> = defaultRules()
) {
    data class DetectionRule(
        val indicatorType: IndicatorType,
        val pattern: Regex,
        val weight: Int,
        val description: String
    )

    companion object {
        fun defaultRules(): List<DetectionRule> {
            return listOf(
                DetectionRule(
                    IndicatorType.URL,
                    Regex("https?://[^\\s\"']+"),
                    15,
                    "Contains an external URL link"
                ),
                DetectionRule(
                    IndicatorType.SHORTENED_URL,
                    Regex("(bit\\.ly|tinyurl\\.com|goo\\.gl|bit\\.do|cut\\.ly|shorte\\.st)/"),
                    25,
                    "Contains a shortened URL"
                ),
                DetectionRule(
                    IndicatorType.ACCOUNT_SUSPENSION,
                    Regex("suspend(ed)?|block(ed)?|close(ed)?|deactivat(e|ed)", RegexOption.IGNORE_CASE),
                    25,
                    "Claims account suspension or blocking"
                ),
                DetectionRule(
                    IndicatorType.OTP_REQUEST,
                    Regex("(otp|one.time.pin|verification code|passcode).{0,50}(verify|enter|provide|input|code)", RegexOption.IGNORE_CASE),
                    30,
                    "Requests OTP or verification code"
                ),
                DetectionRule(
                    IndicatorType.PIN_REQUEST,
                    Regex("(pin|personal ident).{0,30}(verify|enter|provide|input|code|reset)", RegexOption.IGNORE_CASE),
                    40,
                    "Requests PIN"
                ),
                DetectionRule(
                    IndicatorType.BANKING_DETAILS,
                    Regex("(account number|bank detail|ifsc|branch code|sort code).{0,50}(verify|enter|provide|input|update)", RegexOption.IGNORE_CASE),
                    35,
                    "Requests banking details"
                ),
                DetectionRule(
                    IndicatorType.PERSONAL_INFO_REQUEST,
                    Regex("(id number|id no|passport|birth certificate|bod|home address|mobile number|phone number).{0,30}(verify|enter|provide|input|update)", RegexOption.IGNORE_CASE),
                    30,
                    "Requests personal identification information"
                ),
                DetectionRule(
                    IndicatorType.URGENT_LANGUAGE,
                    Regex("(immediate(ly)?|urgent|act now|last chance|final notice|deadline|expires? (soon|today)|within 24 hours)", RegexOption.IGNORE_CASE),
                    10,
                    "Creates urgency or pressure"
                ),
                DetectionRule(
                    IndicatorType.PRIZE_CLAIM,
                    Regex("(congratulations|congrats|lottery|prize|reward|selected|won|jackpot).{0,50}(claim|receive|collect)", RegexOption.IGNORE_CASE),
                    35,
                    "Claims prize or reward"
                ),
                DetectionRule(
                    IndicatorType.FAKE_GRANT,
                    Regex("(grant|sassa|social assistance).{0,50}(suspend|block|claim|expired|payment)", RegexOption.IGNORE_CASE),
                    25,
                    "Fake grant notification"
                ),
                DetectionRule(
                    IndicatorType.PAYMENT_INSTRUCTION,
                    Regex("(transfer|deposit|send money).{0,30}(account|number|bank|verify)", RegexOption.IGNORE_CASE),
                    20,
                    "Requests payment or money transfer"
                ),
                DetectionRule(
                    IndicatorType.SUSPICIOUS_DOMAIN,
                    Regex("\\.(tk|ml|ga|cf|ru|xyz|top|club|online|site)"),
                    20,
                    "Suspicious domain extension"
                )
            )
        }
    }
}

enum class DetectionRuleSet {
    KEYWORDS,
    URL_PATTERNS,
    URGENT_LANGUAGE,
    INFORMATION_REQUESTS,
    SUSPENSION_THREATS,
    SUSPICIOUS_DOMAINS,
    SHORTENED_URLS,
    PAYMENT_INSTRUCTIONS,
    PRIZE_CLAIMS
}
