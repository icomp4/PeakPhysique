package com.peakphysique.app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "steps")
data class StepCount (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "steps") val steps: Long,
    @ColumnInfo(name = "date") val date: String,
    )