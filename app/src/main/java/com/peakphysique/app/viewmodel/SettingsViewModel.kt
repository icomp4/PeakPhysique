package com.peakphysique.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.peakphysique.app.database.repository.SettingsRepository
import com.peakphysique.app.model.Goals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SettingsRepository(application)

    private val _isDarkMode = MutableStateFlow(repository.isDarkMode())
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _goals = MutableStateFlow(repository.getGoals())
    val goals: StateFlow<Goals> = _goals.asStateFlow()

    fun updateDarkMode(enabled: Boolean) {
        repository.setDarkMode(enabled)
        _isDarkMode.value = enabled
    }

    fun updateGoal(
        weightGoal: Float? = null,
        benchGoal: Float? = null,
        squatGoal: Float? = null,
        deadliftGoal: Float? = null
    ) {
        _goals.value = _goals.value.copy(
            weightGoal = weightGoal ?: _goals.value.weightGoal,
            benchGoal = benchGoal ?: _goals.value.benchGoal,
            squatGoal = squatGoal ?: _goals.value.squatGoal,
            deadliftGoal = deadliftGoal ?: _goals.value.deadliftGoal
        )
        repository.updateGoals(_goals.value)
    }
}