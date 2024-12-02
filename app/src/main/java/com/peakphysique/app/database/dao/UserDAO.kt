package com.peakphysique.app.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.peakphysique.app.model.User
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface UserDAO {
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE username = :username")
    fun getUserByUsername(username: String): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    // Alternative delete method if you prefer using username
    @Query("DELETE FROM user WHERE username = :username")
    suspend fun deleteByUsername(username: String)
}