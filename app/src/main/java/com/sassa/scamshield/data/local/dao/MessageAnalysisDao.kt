package com.sassa.scamshield.data.local.dao

import androidx.room.*
import com.sassa.scamshield.data.local.entity.MessageAnalysisEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageAnalysisDao {
    @Query("SELECT * FROM message_analyses ORDER BY analyzedAt DESC")
    fun getAll(): Flow<List<MessageAnalysisEntity>>

    @Query("SELECT * FROM message_analyses WHERE riskScore >= 50 ORDER BY analyzedAt DESC")
    suspend fun getSuspicious(): List<MessageAnalysisEntity>

    @Query("SELECT COUNT(*) FROM message_analyses")
    suspend fun countAll(): Int

    @Query("SELECT COUNT(*) FROM message_analyses WHERE riskScore >= 75")
    suspend fun countHighRisk(): Int

    @Query("SELECT COUNT(*) FROM message_analyses WHERE riskScore < 50")
    suspend fun countSafe(): Int

    @Query("SELECT COUNT(*) FROM message_analyses WHERE riskScore >= 50 AND riskScore < 75")
    suspend fun countSuspicious(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(analysis: MessageAnalysisEntity)

    @Delete
    suspend fun delete(analysis: MessageAnalysisEntity)

    @Query("DELETE FROM message_analyses")
    suspend fun clearAll()
}
