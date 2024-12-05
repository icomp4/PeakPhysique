package com.peakphysique.app.controller

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peakphysique.app.database.repository.SettingsRepository
import com.peakphysique.app.view.*

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Navigation(settingsRepository: SettingsRepository) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        if (settingsRepository.isSurveyCompleted()) {
            navController.navigate("feed_screen") {
                popUpTo("survey_screen") { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = "survey_screen") {
        composable("login_screen") {
            LoginScreen(navController)
        }

        composable("register_screen") {
            RegisterScreen(navController)
        }

        composable("tracking_screen") {
            TrackingScreen(navController)
        }

        composable("feed_screen") {
            FeedScreen(navController)
        }

        composable("account_screen") {
            AccountScreen(navController)
        }

        composable("progress_screen") {
            ProgressScreen(navController)
        }

        composable("history_screen") {
            HistoryScreen(navController)
        }

        composable("log_weight_screen") {
            LogWeightScreen(navController = navController)
        }

        composable("settings_screen") {
            SettingsScreen(navController)
        }

        composable("step_screen") {
            StepCounterScreen(navController)
        }

        composable("survey_screen") {
            FitnessSurveyScreen(navController)
        }
    }
}