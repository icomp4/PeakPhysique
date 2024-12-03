package com.peakphysique.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peakphysique.app.database.dao.UserDAO
import com.peakphysique.app.database.dao.WeightDao
import com.peakphysique.app.model.UserEntity
import com.peakphysique.app.database.dao.WorkoutDao
import com.peakphysique.app.model.Converters
import com.peakphysique.app.model.WorkoutEntity
import com.peakphysique.app.model.SetEntity
import com.peakphysique.app.model.WeightEntity

@Database(
    entities = [
        UserEntity::class,
        WorkoutEntity::class,
        SetEntity::class,
        WeightEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun workoutDao(): WorkoutDao
    abstract fun weightDao(): WeightDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitness_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}