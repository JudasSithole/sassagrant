package com.sassa.scamshield.data.local.dao

import androidx.room.*
import com.sassa.scamshield.data.local.entity.ScamNumberEntity
import com.sassa.scamshield.util.ScamCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ScamNumberDao {
    @Query("SELECT * FROM scam_numbers ORDER BY lastReported DESC")
    fun getAllNumbers(): Flow<List<ScamNumberEntity>>

    @Query("SELECT * FROM scam_numbers WHERE phoneNumber = :phoneNumber LIMIT 1")
    suspend fun getNumber(phoneNumber: String): ScamNumberEntity?

    @Query("SELECT * FROM scam_numbers WHERE riskScore >= :minScore ORDER BY riskScore DESC")
    suspend fun getNumbersAboveScore(minScore: Int): List<ScamNumberEntity>

    @Query("SELECT * FROM scam_numbers WHERE category = :category ORDER BY lastReported DESC")
    fun getNumbersByCategory(category: ScamCategory): Flow<List<ScamNumberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(number: ScamNumberEntity)

    @Update
    suspend fun update(number: ScamNumberEntity)

    @Delete
    suspend fun delete(number: ScamNumberEntity)

    @Query("DELETE FROM scam_numbers")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM scam_numbers")
    suspend fun count(): Int
}
