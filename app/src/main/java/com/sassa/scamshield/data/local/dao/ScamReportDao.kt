package com.sassa.scamshield.data.local.dao

import androidx.room.*
import com.sassa.scamshield.data.local.entity.ScamReportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScamReportDao {
    @Query("SELECT * FROM scam_reports ORDER BY submittedAt DESC")
    fun getAllReports(): Flow<List<ScamReportEntity>>

    @Query("SELECT * FROM scam_reports WHERE phoneNumber = :phoneNumber ORDER BY submittedAt DESC")
    suspend fun getReportsForNumber(phoneNumber: String): List<ScamReportEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(report: ScamReportEntity)

    @Update
    suspend fun update(report: ScamReportEntity)

    @Delete
    suspend fun delete(report: ScamReportEntity)

    @Query("DELETE FROM scam_reports")
    suspend fun clearAll()
}
