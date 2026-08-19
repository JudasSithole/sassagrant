package com.sassa.scamshield.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sassa.scamshield.data.local.dao.MessageAnalysisDao
import com.sassa.scamshield.data.local.dao.ScamNumberDao
import com.sassa.scamshield.data.local.dao.ScamReportDao
import com.sassa.scamshield.data.local.dao.SecurityEventDao
import com.sassa.scamshield.data.local.entity.MessageAnalysisEntity
import com.sassa.scamshield.data.local.entity.ScamNumberEntity
import com.sassa.scamshield.data.local.entity.ScamReportEntity
import com.sassa.scamshield.data.local.entity.SecurityEventEntity

@Database(
    entities = [
        ScamNumberEntity::class,
        ScamReportEntity::class,
        MessageAnalysisEntity::class,
        SecurityEventEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ScamShieldDatabase : RoomDatabase() {
    abstract fun scamNumberDao(): ScamNumberDao
    abstract fun scamReportDao(): ScamReportDao
    abstract fun messageAnalysisDao(): MessageAnalysisDao
    abstract fun securityEventDao(): SecurityEventDao
}
