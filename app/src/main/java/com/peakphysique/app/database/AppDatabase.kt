package com.peakphysique.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.peakphysique.app.controller.UserController
import com.peakphysique.app.model.User

@Database(entities = [User::class], version = 1, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userController(): UserController
    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    var instance = INSTANCE
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "app_database"
                        ).fallbackToDestructiveMigration()
                            .build()
                        INSTANCE = instance
                    }
                }
            }
            return INSTANCE!!
        }
    }
}