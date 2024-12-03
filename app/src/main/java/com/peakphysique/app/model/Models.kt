package com.peakphysique.app.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDateTime

/**
 * Represents a user in the database.
 * Contains personal information and authentication details.
 *
 * @property username User's display name
 * @property email User's email address (used for authentication)
 * @property password User's hashed password
 * @property height User's height in centimeters
 * @property weight User's weight in kilograms
 * @property age User's age in years
 * @property createdAt Timestamp of account creation (Unix timestamp)
 * @property id Auto-generated primary key
 */
@Entity(tableName = "users")
data class UserEntity(
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "age") val age: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}

/**
 * Represents a workout session in the database.
 * Acts as a container for related exercise sets.
 *
 * @property id Unique identifier (UUID)
 * @property date Date and time when the workout was performed
 */
@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey val id: String,
    val date: LocalDateTime,
)

/**
 * Represents a single exercise set within a workout.
 * Links to its parent workout via foreign key relationship.
 *
 * @property id Unique identifier (UUID)
 * @property workoutId Reference to parent workout
 * @property name Name of the exercise
 * @property reps Number of repetitions performed
 * @property weight Weight used for the exercise
 * @property notes Additional notes or comments about the set
 */
@Entity(
    tableName = "sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE  // Deletes sets when parent workout is deleted
        )
    ],
    indices = [Index("workoutId")]  // Index for foreign key performance
)
data class SetEntity(
    @PrimaryKey
    val id: String,
    val workoutId: String,
    val name: String,
    val reps: String,
    val weight: String,
    val notes: String
)

/**
 * Represents a workout with its associated sets.
 * Used for queries that need to retrieve a workout together with all its sets.
 *
 * @property workout The workout entity
 * @property sets List of sets belonging to the workout
 */
data class WorkoutWithSets(
    @Embedded val workout: WorkoutEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId"
    )
    val sets: List<SetEntity>
)

/**
 * Data transfer object (DTO) for workout sets.
 * Used for in-memory representation of sets before they are saved to the database.
 *
 * @property id Unique identifier for the set
 * @property name Exercise name
 * @property reps Number of repetitions
 * @property weight Weight used
 * @property notes Additional notes
 */
data class WorkoutSet(
    val id: String,
    val name: String,
    val reps: String,
    val weight: String,
    val notes: String
)