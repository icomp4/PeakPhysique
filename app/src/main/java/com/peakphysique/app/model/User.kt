/*
    Example model class, should define the structure of the data that will be stored in the database
 */

package com.peakphysique.app.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "username")
    var username: String = ""

    @ColumnInfo(name = "email")
    var email: String = ""

    @ColumnInfo(name = "password")
    var password: String = ""
}