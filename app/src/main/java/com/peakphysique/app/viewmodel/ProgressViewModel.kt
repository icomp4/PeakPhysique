package com.peakphysique.app.viewmodel

import WorkoutRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.peakphysique.app.database.AppDatabase
import com.peakphysique.app.database.repository.SettingsRepository
import com.peakphysique.app.database.repository.WeightRepository
import com.peakphysique.app.model.Goals
import com.peakphysique.app.model.WorkoutWithSets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ProgressViewModel(application: Application) : AndroidViewModel(application) {
    private val workoutRepository: WorkoutRepository
    private val weightRepository: WeightRepository
    private val settingsRepository: SettingsRepository

    private val _currentWeight = MutableStateFlow<Float?>(null)
    val currentWeight: StateFlow<Float?> = _currentWeight.asStateFlow()

    private val _startingWeight = MutableStateFlow<Float?>(null)
    val startingWeight: StateFlow<Float?> = _startingWeight.asStateFlow()

    private val _goalWeight = MutableStateFlow<Float>(0f)
    val goalWeight: StateFlow<Float> = _goalWeight.asStateFlow()

    private val _strengthProgress = MutableStateFlow<Map<String, Pair<Float, Float>>>(emptyMap())
    val strengthProgress: StateFlow<Map<String, Pair<Float, Float>>> = _strengthProgress.asStateFlow()

    private val _monthlyWorkouts = MutableStateFlow(0)
    val monthlyWorkouts: StateFlow<Int> = _monthlyWorkouts.asStateFlow()

    private val _monthlyPRs = MutableStateFlow(0)
    val monthlyPRs: StateFlow<Int> = _monthlyPRs.asStateFlow()

    private val _strengthGoals = MutableStateFlow(Goals())
    val strengthGoals: StateFlow<Goals> = _strengthGoals.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        workoutRepository = WorkoutRepository(database.workoutDao())
        weightRepository = WeightRepository(database.weightDao())
        settingsRepository = SettingsRepository(application)

        loadWeightProgress()
        loadStrengthProgress()
        loadMonthlyStats()
        observeGoals()
    }

    private fun observeGoals() {
        viewModelScope.launch {
            val goals = settingsRepository.getGoals()
            _goalWeight.value = goals.weightGoal
            _strengthGoals.value = goals
        }
    }

    private fun loadWeightProgress() {
        viewModelScope.launch {
            weightRepository.allWeights.collect { weights ->
                if (weights.isNotEmpty()) {
                    _currentWeight.value = weights.first().weight
                    _startingWeight.value = weights.lastOrNull()?.weight
                }
            }
        }
    }

    private fun loadStrengthProgress() {
        viewModelScope.launch {
            workoutRepository.allWorkouts.collect { workouts ->
                val exerciseProgress = mutableMapOf<String, Pair<Float, Float>>()

                val exerciseSets = workouts.flatMap { it.sets }
                    .groupBy { it.name }

                exerciseSets.forEach { (exercise, sets) ->
                    val weightsList = sets.mapNotNull { it.weight.toFloatOrNull() }
                    if (weightsList.isNotEmpty()) {
                        val startWeight = weightsList.minOrNull() ?: 0f
                        val currentWeight = weightsList.maxOrNull() ?: 0f

                        exerciseProgress[exercise] = Pair(startWeight, currentWeight)
                    }
                }

                _strengthProgress.value = exerciseProgress
            }
        }
    }

    private fun loadMonthlyStats() {
        viewModelScope.launch {
            workoutRepository.allWorkouts.collect { workouts ->
                val thisMonth = LocalDateTime.now().withDayOfMonth(1)

                val monthlyWorkoutCount = workouts.count {
                    it.workout.date.isAfter(thisMonth)
                }
                _monthlyWorkouts.value = monthlyWorkoutCount

                val monthlyPRCount = calculateMonthlyPRs(workouts, thisMonth)
                _monthlyPRs.value = monthlyPRCount
            }
        }
    }

    private fun calculateMonthlyPRs(
        workouts: List<WorkoutWithSets>,
        thisMonth: LocalDateTime
    ): Int {
        var prCount = 0
        val exerciseMaxes = mutableMapOf<String, Float>()

        val sortedWorkouts = workouts.sortedBy { it.workout.date }

        sortedWorkouts.forEach { workout ->
            if (workout.workout.date.isAfter(thisMonth)) {
                workout.sets.forEach { set ->
                    val weight = set.weight.toFloatOrNull() ?: 0f
                    val currentMax = exerciseMaxes[set.name] ?: 0f

                    if (weight > currentMax) {
                        exerciseMaxes[set.name] = weight
                        prCount++
                    }
                }
            }
        }
        return prCount
    }
}