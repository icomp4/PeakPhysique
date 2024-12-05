package com.peakphysique.app.database.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peakphysique.app.model.StepCount

interface StepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stepCount: StepCount)

    @Query("SELECT * FROM steps ORDER BY date DESC")
    suspend fun getAllStepCounts(): List<StepCount>

    @Query("SELECT * FROM steps WHERE date = :date LIMIT 1")
    suspend fun getStepCountByDate(date: String): StepCount?
}