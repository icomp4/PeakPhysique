package com.peakphysique.app.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.peakphysique.app.model.WeightEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface WeightDao {
    @Insert
    suspend fun insertWeight(weight: WeightEntity)

    @Query("SELECT * FROM weight_logs ORDER BY date DESC")
    fun getAllWeights(): Flow<List<WeightEntity>>

    @Query("SELECT * FROM weight_logs ORDER BY date DESC LIMIT 1")
    suspend fun getLatestWeight(): WeightEntity?

    @Query("SELECT * FROM weight_logs WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getWeightsInRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<WeightEntity>>
}