package com.peakphysique.app

import WorkoutRepository
import android.content.Context
import com.peakphysique.app.database.AppDatabase

/**
 * Retrieves the ID of the currently authenticated user.
 *
 * @param current The application context
 * @return The user ID of the currently authenticated user
 */
fun getCurrentUserId(current: Context): Int {
    return 0
}

/**
 * Creates and returns a WorkoutRepository instance.
 * This factory function ensures consistent repository initialization across the app.
 *
 * @param context The application context used to access the database
 * @return A configured WorkoutRepository instance connected to the app's database
 */
fun getWorkoutRepository(context: Context): WorkoutRepository {
    val database = AppDatabase.getDatabase(context)
    return WorkoutRepository(database.workoutDao())
}