package com.peakphysique.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.peakphysique.app.database.repository.SettingsRepository
import com.peakphysique.app.model.Goals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SurveyViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SettingsRepository(application)

    private val _displayName = MutableStateFlow("")
    val displayName: StateFlow<String> = _displayName.asStateFlow()

    private val _goals = MutableStateFlow(Goals())
    val goals: StateFlow<Goals> = _goals.asStateFlow()

    fun updateName(name: String) {
        _displayName.value = name
    }

    fun updateGoals(
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
    }

    fun saveSurveyData() {
        repository.setDisplayName(_displayName.value)
        repository.updateGoals(_goals.value)
        repository.setSurveyCompleted()
    }

}