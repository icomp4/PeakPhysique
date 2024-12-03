package com.peakphysique.app.controller

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peakphysique.app.view.AccountScreen
import com.peakphysique.app.view.FeedScreen
import com.peakphysique.app.view.HistoryScreen
import com.peakphysique.app.view.LogWeightScreen
import com.peakphysique.app.view.LoginScreen
import com.peakphysique.app.view.ProgressScreen
import com.peakphysique.app.view.RegisterScreen
import com.peakphysique.app.view.TrackingScreen

/**
 * Main navigation component for the application.
 * Defines the navigation graph and sets up all possible screen destinations.
 *
 * Navigation Flow:
 * - Starts at login_screen (authentication required)
 * - After authentication, users can navigate to:
 *   * Main features: tracking, feed, account, progress
 *   * History tracking and analysis
 *   * Weight logging
 *   * Account management
 */
@Composable
fun Navigation() {
    // Create and remember NavController instance for handling navigation
    val navController = rememberNavController()

    // Define the navigation graph with login as the start destination
    NavHost(navController = navController, startDestination = "login_screen") {

        // Authentication screens
        composable("login_screen") {
            LoginScreen(navController)
        }

        composable("register_screen") {
            RegisterScreen(navController)
        }

        // Main feature screens
        composable("tracking_screen") {
            TrackingScreen(navController)
        }

        composable("feed_screen") {
            FeedScreen(navController)
        }

        composable("account_screen") {
            AccountScreen(navController)
        }

        // Progress tracking and analysis screens
        composable("progress_screen") {
            ProgressScreen(navController)
        }

        composable("history_screen") {
            HistoryScreen(navController)
        }

        composable("log_weight_screen") {
            LogWeightScreen(navController = navController)
        }
    }
}