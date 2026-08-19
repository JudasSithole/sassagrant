package com.sassa.scamshield.data.local.database

import androidx.room.TypeConverter
import com.sassa.scamshield.util.RiskLevel
import com.sassa.scamshield.util.ScamCategory

class Converters {
    @TypeConverter
    fun fromRiskLevel(value: RiskLevel): String = value.label

    @TypeConverter
    fun toRiskLevel(value: String): RiskLevel = RiskLevel.fromString(value)

    @TypeConverter
    fun fromScamCategory(value: ScamCategory): String = value.name

    @TypeConverter
    fun toScamCategory(value: String): ScamCategory =
        ScamCategory.values().find { it.name == value } ?: ScamCategory.OTHER
}
