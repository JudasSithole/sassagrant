package com.sassa.scamshield.data.local.dao

import androidx.room.*
import com.sassa.scamshield.data.local.entity.SecurityEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SecurityEventDao {
    @Query("SELECT * FROM security_events ORDER BY timestamp DESC")
    fun getAllEvents(): Flow<List<SecurityEventEntity>>

    @Query("SELECT * FROM security_events WHERE timestamp >= :since ORDER BY timestamp DESC")
    suspend fun getEventsSince(since: Long): List<SecurityEventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: SecurityEventEntity)

    @Query("DELETE FROM security_events")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM security_events WHERE riskLevel IN ('HIGH_RISK', 'KNOWN_SCAM')")
    suspend fun countThreatsBlocked(): Int

    @Query("SELECT COUNT(*) FROM security_events WHERE eventType = 'MESSAGE_DETECTED' OR eventType = 'MESSAGE_FLAGGED'")
    suspend fun countSuspiciousMessages(): Int

    @Query("SELECT COUNT(*) FROM security_events WHERE eventType = 'CALL_FLAGGED' OR eventType = 'CALL_BLOCKED'")
    suspend fun countSuspiciousCalls(): Int

    @Query("SELECT * FROM security_events ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestEvent(): SecurityEventEntity?
}
