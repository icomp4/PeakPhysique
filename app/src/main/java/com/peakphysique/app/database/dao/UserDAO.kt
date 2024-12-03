package com.peakphysique.app.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.peakphysique.app.model.UserEntity

/**
 * Data Access Object for user-related database operations.
 * Handles all database interactions for user accounts and profiles.
 */
@Dao
interface UserDAO {
    /**
     * Retrieves all users from the database.
     * Returns LiveData for reactive updates in the UI.
     *
     * @return LiveData containing list of all users
     */
    @Query("SELECT * FROM users")
    fun getAll(): LiveData<List<UserEntity>>

    /**
     * Retrieves a specific user by their ID.
     * Returns LiveData for reactive updates in the UI.
     *
     * @param id The unique identifier of the user
     * @return LiveData containing the requested user
     */
    @Query("SELECT * FROM users WHERE id = :id")
    fun getById(id: Int): LiveData<UserEntity>

    /**
     * Inserts or updates a user in the database.
     * Uses REPLACE strategy to handle conflicts, effectively updating existing users.
     *
     * @param user The user entity to insert or update
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    /**
     * Deletes a user from the database.
     * Should be used carefully as it permanently removes the user data.
     *
     * @param user The user entity to delete
     */
    @Delete
    suspend fun delete(user: UserEntity)
}