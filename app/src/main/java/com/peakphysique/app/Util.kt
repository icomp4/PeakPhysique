package com.peakphysique.app

import WorkoutRepository
import android.content.Context
import com.peakphysique.app.database.AppDatabase

fun getCurrentUserId(current: Context): Int {
    return 0
}
fun getWorkoutRepository(context: Context): WorkoutRepository {
    val database = AppDatabase.getDatabase(context)
    return WorkoutRepository(database.workoutDao())
}