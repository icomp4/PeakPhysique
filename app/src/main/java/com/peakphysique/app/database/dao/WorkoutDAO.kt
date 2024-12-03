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

/**
 * Data Access Object for workout-related database operations.
 * Provides methods to interact with workouts and their associated sets.
 */
@Dao
interface WorkoutDao {
    /**
     * Retrieves all workouts with their associated sets.
     * Results are ordered by date in descending order (newest first).
     *
     * @return Flow emitting list of workouts with their sets
     */
    @Transaction
    @Query("SELECT * FROM workouts ORDER BY date DESC")
    fun getAllWorkoutsWithSets(): Flow<List<WorkoutWithSets>>

    /**
     * Retrieves workouts within a specified date range.
     * Used for filtering workout history by date.
     *
     * @param startDate Beginning of the date range
     * @param endDate End of the date range
     * @return Flow emitting list of workouts within the range
     */
    @Query("SELECT * FROM workouts WHERE date >= :startDate AND date <= :endDate")
    fun getWorkoutsInRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<WorkoutWithSets>>

    /**
     * Inserts or updates a workout in the database.
     * Uses REPLACE strategy for conflicts to ensure idempotency.
     *
     * @param workout The workout to insert or update
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity)

    /**
     * Inserts or updates multiple sets in the database.
     * Uses REPLACE strategy for conflicts to ensure idempotency.
     *
     * @param sets List of sets to insert or update
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSets(sets: List<SetEntity>)

    /**
     * Deletes a workout from the database.
     * Associated sets are automatically deleted due to CASCADE delete rule.
     *
     * @param workout The workout to delete
     */
    @Delete
    suspend fun deleteWorkout(workout: WorkoutEntity)

    /**
     * Transactionally inserts a workout and its associated sets.
     * Ensures data consistency by wrapping both operations in a transaction.
     *
     * @param workout The workout to insert
     * @param sets The associated sets to insert
     */
    @Transaction
    suspend fun insertWorkoutWithSets(workout: WorkoutEntity, sets: List<SetEntity>) {
        insertWorkout(workout)
        insertSets(sets)
    }
}