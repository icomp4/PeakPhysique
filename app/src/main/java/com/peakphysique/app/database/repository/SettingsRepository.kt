package com.peakphysique.app.database.repository

import android.content.Context
import android.content.SharedPreferences
import com.peakphysique.app.model.Goals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepository(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _goals = MutableStateFlow(getGoals())
    val goalsFlow: StateFlow<Goals> = _goals.asStateFlow()

    private val prefListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key != null) {
            when {
                key.startsWith("weight_goal") ||
                        key.startsWith("bench_goal") ||
                        key.startsWith("squat_goal") ||
                        key.startsWith("deadlift_goal") -> {
                    _goals.value = getGoals()
                }
            }
        }
    }

    init {
        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefListener)
    }

    fun isDarkMode(): Boolean {
        return sharedPreferences.getBoolean("is_dark_mode", false)
    }

    fun setDarkMode(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("is_dark_mode", enabled).apply()
    }

    fun getDisplayName(): String {
        return sharedPreferences.getString("display_name", "User") ?: "User"
    }

    fun setDisplayName(name: String) {
        sharedPreferences.edit().putString("display_name", name).apply()
    }

    fun getGoals(): Goals {
        return Goals(
            weightGoal = sharedPreferences.getFloat("weight_goal", 0f),
            benchGoal = sharedPreferences.getFloat("bench_goal", 0f),
            squatGoal = sharedPreferences.getFloat("squat_goal", 0f),
            deadliftGoal = sharedPreferences.getFloat("deadlift_goal", 0f)
        )
    }

    fun updateGoals(goals: Goals) {
        sharedPreferences.edit().apply {
            putFloat("weight_goal", goals.weightGoal)
            putFloat("bench_goal", goals.benchGoal)
            putFloat("squat_goal", goals.squatGoal)
            putFloat("deadlift_goal", goals.deadliftGoal)
            apply()
        }
        _goals.value = goals
    }
}