package com.peakphysique.app.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.peakphysique.app.model.UserEntity

@Dao
interface UserDAO {
    @Query("SELECT * FROM users")
    fun getAll(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getById(id: Int): LiveData<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

}