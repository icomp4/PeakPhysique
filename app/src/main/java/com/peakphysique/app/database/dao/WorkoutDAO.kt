package com.peakphysique.app.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.peakphysique.app.model.SetEntity
import com.peakphysique.app.model.WorkoutEntity
import com.peakphysique.app.model.WorkoutWithSets
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface WorkoutDao {
    @Transaction
    @Query("SELECT * FROM workouts ORDER BY date DESC")
    fun getAllWorkoutsWithSets(): Flow<List<WorkoutWithSets>>

    @Query("SELECT * FROM workouts WHERE date >= :startDate AND date <= :endDate")
    fun getWorkoutsInRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<WorkoutWithSets>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSets(sets: List<SetEntity>)

    @Delete
    suspend fun deleteWorkout(workout: WorkoutEntity)

    @Transaction
    suspend fun insertWorkoutWithSets(workout: WorkoutEntity, sets: List<SetEntity>) {
        insertWorkout(workout)
        insertSets(sets)
    }
}