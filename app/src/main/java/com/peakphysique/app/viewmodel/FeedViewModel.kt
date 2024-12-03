/**
 * FeedViewModel manages the social feed data for the Peak Physique app.
 * It processes workout data to generate meaningful feed entries about user achievements,
 * including personal records (PRs) and workout streaks.
 *
 * Key features:
 * - Tracks and announces new personal records
 * - Monitors workout streaks
 * - Provides relative time formatting for achievements
 * - Maintains sorted feed entries based on timestamp
 */
package com.peakphysique.app.viewmodel

import WorkoutRepository
import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.peakphysique.app.database.AppDatabase
import com.peakphysique.app.model.WorkoutWithSets
import com.peakphysique.app.view.FeedEntryData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class FeedViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    // StateFlow to manage feed entries with public immutable exposure
    private val _feedEntries = MutableStateFlow<List<FeedEntryData>>(emptyList())
    val feedEntries: StateFlow<List<FeedEntryData>> = _feedEntries.asStateFlow()

    init {
        // Initialize feed data processing in coroutine scope
        viewModelScope.launch {
            workoutRepository.allWorkouts.collect { workouts ->
                val newEntries = mutableListOf<FeedEntryData>()

                workouts.forEach { currentWorkout ->
                    // Group sets by exercise name for analysis
                    val exerciseSets = currentWorkout.sets.groupBy { it.name }

                    exerciseSets.forEach { (exerciseName, sets) ->
                        // Find maximum weight for current exercise
                        val maxWeight = sets.maxOfOrNull { it.weight.toDoubleOrNull() ?: 0.0 } ?: 0.0

                        // Find previous maximum weight for comparison
                        val previousMax = workouts
                            .filter { it.workout.date < currentWorkout.workout.date }
                            .flatMap { it.sets }
                            .filter { it.name == exerciseName }
                            .mapNotNull { it.weight.toDoubleOrNull() }
                            .maxOrNull() ?: 0.0

                        // Create feed entry for new personal records
                        if (maxWeight > previousMax && previousMax > 0) {
                            val increase = ((maxWeight - previousMax) / previousMax * 100).toInt()
                            newEntries.add(
                                FeedEntryData(
                                    type = "New $exerciseName PR!",
                                    achievement = "${maxWeight}lbs - That's a $increase% increase from your previous best!",
                                    time = getTimeAgo(currentWorkout.workout.date),
                                    icon = Icons.Default.Favorite
                                )
                            )
                        }
                    }

                    // Check and add workout streak achievements
                    checkWorkoutStreak(workouts)?.let {
                        newEntries.add(it)
                    }
                }

                // Sort entries by time, converting relative time strings to actual timestamps
                _feedEntries.value = newEntries.sortedByDescending { feedEntry ->
                    when {
                        feedEntry.time.contains("Just now") -> LocalDateTime.now()
                        feedEntry.time.contains("hours") -> LocalDateTime.now().minusHours(
                            feedEntry.time.split(" ")[0].toLongOrNull() ?: 0
                        )
                        feedEntry.time.contains("days") -> LocalDateTime.now().minusDays(
                            feedEntry.time.split(" ")[0].toLongOrNull() ?: 0
                        )
                        feedEntry.time.contains("months") -> LocalDateTime.now().minusMonths(
                            feedEntry.time.split(" ")[0].toLongOrNull() ?: 0
                        )
                        else -> LocalDateTime.now()
                    }
                }
            }
        }
    }

    /**
     * Analyzes workout dates to identify and create achievements for workout streaks.
     * A streak is counted when workouts occur on consecutive days.
     *
     * @param workouts List of workouts to analyze
     * @return FeedEntryData for significant streaks (5+ days) or null if no significant streak found
     */
    private fun checkWorkoutStreak(workouts: List<WorkoutWithSets>): FeedEntryData? {
        val sortedDates = workouts.map { it.workout.date }.sorted()
        if (sortedDates.isEmpty()) return null

        var currentStreak = 1
        var maxStreak = 1

        // Calculate longest streak of consecutive workout days
        for (i in 1 until sortedDates.size) {
            val daysBetween = ChronoUnit.DAYS.between(sortedDates[i-1], sortedDates[i])
            if (daysBetween == 1L) {
                currentStreak++
                maxStreak = maxOf(maxStreak, currentStreak)
            } else {
                currentStreak = 1
            }
        }

        // Create achievement entry for significant streaks
        return when {
            maxStreak >= 5 -> FeedEntryData(
                type = "You're on fire!",
                achievement = "You've worked out $maxStreak days in a row!",
                time = getTimeAgo(sortedDates.last()),
                icon = Icons.Default.Star
            )
            else -> null
        }
    }

    /**
     * Converts a DateTime to a human-readable relative time string.
     * Examples: "Just now", "5 hours ago", "2 days ago", "3 months ago"
     *
     * @param dateTime The DateTime to convert
     * @return String representing the relative time
     */
    private fun getTimeAgo(dateTime: LocalDateTime): String {
        val now = LocalDateTime.now()
        val hours = ChronoUnit.HOURS.between(dateTime, now)
        val days = ChronoUnit.DAYS.between(dateTime, now)

        return when {
            hours < 1 -> "Just now"
            hours < 24 -> "$hours hours ago"
            days < 30 -> "$days days ago"
            else -> "${days / 30} months ago"
        }
    }
}

/**
 * Factory for creating FeedViewModel instances with proper dependencies.
 * Provides database and repository access to the ViewModel.
 */
class FeedViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            val database = AppDatabase.getDatabase(application)
            val repository = WorkoutRepository(database.workoutDao())
            @Suppress("UNCHECKED_CAST")
            return FeedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        // Factory provider using the newer viewModelFactory DSL
        fun provide(application: Application) = viewModelFactory {
            initializer {
                val database = AppDatabase.getDatabase(application)
                val repository = WorkoutRepository(database.workoutDao())
                FeedViewModel(repository)
            }
        }
    }
}