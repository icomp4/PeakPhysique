package com.peakphysique.app.viewmodel

import WorkoutRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.peakphysique.app.database.AppDatabase
import com.peakphysique.app.model.WorkoutWithSets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.temporal.ChronoUnit

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val workoutRepository: WorkoutRepository

    private val _totalWorkouts = MutableStateFlow(0)
    val totalWorkouts: StateFlow<Int> = _totalWorkouts.asStateFlow()

    private val _recordsBroken = MutableStateFlow(0)
    val recordsBroken: StateFlow<Int> = _recordsBroken.asStateFlow()

    private val _monthsActive = MutableStateFlow(0)
    val monthsActive: StateFlow<Int> = _monthsActive.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        workoutRepository = WorkoutRepository(database.workoutDao())
        loadWorkoutStats()
    }

    private fun loadWorkoutStats() {
        viewModelScope.launch {
            workoutRepository.allWorkouts.collect { workouts ->
                _totalWorkouts.value = workouts.size

                calculateRecordsBroken(workouts)

                calculateMonthsActive(workouts)
            }
        }
    }

    private fun calculateRecordsBroken(workouts: List<WorkoutWithSets>) {
        val exerciseMaxes = mutableMapOf<String, Float>()
        var prCount = 0

        val sortedWorkouts = workouts.sortedBy { it.workout.date }

        sortedWorkouts.forEach { workout ->
            workout.sets.forEach { set ->
                val weight = set.weight.toFloatOrNull() ?: 0f
                val currentMax = exerciseMaxes[set.name] ?: 0f

                if (weight > currentMax) {
                    exerciseMaxes[set.name] = weight
                    prCount++
                }
            }
        }

        _recordsBroken.value = prCount
    }

    private fun calculateMonthsActive(workouts: List<WorkoutWithSets>) {
        if (workouts.isEmpty()) {
            _monthsActive.value = 0
            return
        }

        val firstWorkoutDate = workouts.minOf { it.workout.date }
        val lastWorkoutDate = workouts.maxOf { it.workout.date }

        val months = ChronoUnit.MONTHS.between(
            firstWorkoutDate.withDayOfMonth(1),
            lastWorkoutDate.withDayOfMonth(1)
        ).toInt() + 1

        _monthsActive.value = months
    }
}
