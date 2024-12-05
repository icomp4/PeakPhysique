package com.peakphysique.app.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDateTime

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

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey val id: String,
    val date: LocalDateTime,
)

@Entity(tableName = "weight_logs")
data class WeightEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val weight: Float,
    val date: LocalDateTime
)


@Entity(
    tableName = "sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workoutId")]
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

data class WorkoutWithSets(
    @Embedded val workout: WorkoutEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId"
    )
    val sets: List<SetEntity>
)

data class WorkoutSet(
    val id: String,
    val name: String,
    val reps: String,
    val weight: String,
    val notes: String
)

data class Goals(
    val weightGoal: Float = 0f,
    val benchGoal: Float = 0f,
    val squatGoal: Float = 0f,
    val deadliftGoal: Float = 0f
)