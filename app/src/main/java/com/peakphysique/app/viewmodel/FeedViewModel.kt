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
    private val _feedEntries = MutableStateFlow<List<FeedEntryData>>(emptyList())
    val feedEntries: StateFlow<List<FeedEntryData>> = _feedEntries.asStateFlow()

    init {
        viewModelScope.launch {
            workoutRepository.allWorkouts.collect { workouts ->
                val newEntries = mutableListOf<FeedEntryData>()

                workouts.forEach { currentWorkout ->
                    val exerciseSets = currentWorkout.sets.groupBy { it.name }

                    exerciseSets.forEach { (exerciseName, sets) ->
                        val maxWeight = sets.maxOfOrNull { it.weight.toDoubleOrNull() ?: 0.0 } ?: 0.0

                        val previousMax = workouts
                            .filter { it.workout.date < currentWorkout.workout.date }
                            .flatMap { it.sets }
                            .filter { it.name == exerciseName }
                            .mapNotNull { it.weight.toDoubleOrNull() }
                            .maxOrNull() ?: 0.0

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

                    checkWorkoutStreak(workouts)?.let {
                        newEntries.add(it)
                    }
                }

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

    private fun checkWorkoutStreak(workouts: List<WorkoutWithSets>): FeedEntryData? {
        val sortedDates = workouts.map { it.workout.date }.sorted()
        if (sortedDates.isEmpty()) return null

        var currentStreak = 1
        var maxStreak = 1

        for (i in 1 until sortedDates.size) {
            val daysBetween = ChronoUnit.DAYS.between(sortedDates[i-1], sortedDates[i])
            if (daysBetween == 1L) {
                currentStreak++
                maxStreak = maxOf(maxStreak, currentStreak)
            } else {
                currentStreak = 1
            }
        }

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
        fun provide(application: Application) = viewModelFactory {
            initializer {
                val database = AppDatabase.getDatabase(application)
                val repository = WorkoutRepository(database.workoutDao())
                FeedViewModel(repository)
            }
        }
    }
}