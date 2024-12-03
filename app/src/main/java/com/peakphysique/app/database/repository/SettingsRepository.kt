package com.peakphysique.app.database.repository

import android.content.Context
import com.peakphysique.app.model.Goals

class SettingsRepository(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun isDarkMode(): Boolean {
        return sharedPreferences.getBoolean("is_dark_mode", false)
    }

    fun setDarkMode(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("is_dark_mode", enabled).apply()
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
    }
}