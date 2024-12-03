package com.peakphysique.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peakphysique.app.database.dao.UserDAO
import com.peakphysique.app.model.UserEntity
import com.peakphysique.app.database.dao.WorkoutDao
import com.peakphysique.app.model.Converters
import com.peakphysique.app.model.WorkoutEntity
import com.peakphysique.app.model.SetEntity

/**
 * Main database class for the application.
 * Configures Room database with all entities and type converters.
 *
 * Database Schema:
 * - Users: Stores user profiles and authentication data
 * - Workouts: Stores workout sessions
 * - Sets: Stores exercise sets linked to workouts
 */
@Database(
    entities = [
        UserEntity::class,
        WorkoutEntity::class,
        SetEntity::class
    ],
    version = 1,
    exportSchema = false  // Disable schema export for version control
)
@TypeConverters(Converters::class)  // Register custom type converters
abstract class AppDatabase : RoomDatabase() {
    /**
     * Provides access to user-related database operations
     */
    abstract fun userDao(): UserDAO

    /**
     * Provides access to workout-related database operations
     */
    abstract fun workoutDao(): WorkoutDao

    companion object {
        /**
         * Singleton instance of the database
         * Volatile ensures atomic reads and writes to the instance
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Gets the singleton database instance.
         * Creates the database on first access using double-checked locking pattern.
         *
         * @param context Application context used to create the database
         * @return Singleton instance of AppDatabase
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitness_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}