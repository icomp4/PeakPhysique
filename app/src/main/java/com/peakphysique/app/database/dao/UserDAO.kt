package com.peakphysique.app.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.peakphysique.app.model.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE username = :username")
    fun getUserByUsername(username: String): LiveData<User>

    @Query("INSERT INTO user (username, email, password) VALUES (:username, :email, :password)")
    fun insert(username: String, email: String, password: String)

    @Query("DELETE FROM user WHERE username = :username")
    fun delete(username: String)

}