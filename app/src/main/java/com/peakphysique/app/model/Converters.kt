package com.peakphysique.app.model

import androidx.room.TypeConverter
import java.time.LocalDateTime

/**
 * Type converters for Room database.
 * Handles conversion between SQLite-supported types and custom types used in entities.
 */
class Converters {
    /**
     * Converts a string timestamp from the database into a LocalDateTime object.
     * Used when reading datetime values from the database.
     *
     * @param value The ISO-8601 formatted datetime string from the database
     * @return LocalDateTime object, or null if input is null
     */
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    /**
     * Converts a LocalDateTime object into a string for database storage.
     * Used when writing datetime values to the database.
     *
     * @param date The LocalDateTime object to convert
     * @return ISO-8601 formatted string representation, or null if input is null
     */
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}