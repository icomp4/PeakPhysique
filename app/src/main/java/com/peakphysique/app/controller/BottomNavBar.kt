package com.peakphysique.app.controller

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Defines the main navigation routes in the application.
 * Each route represents a distinct screen or destination.
 */
sealed class Routes(val route: String) {
    data object Track : Routes("tracking_screen")    // Workout tracking screen
    data object History : Routes("history_screen")   // Workout history screen
    data object Feed : Routes("feed_screen")         // Social feed screen
    data object Account : Routes("account_screen")   // User account screen
}

/**
 * Enum class defining the bottom navigation bar items.
 * Each screen is represented with its route, display title, and icon.
 *
 * @property route The navigation route for the screen
 * @property title The display text shown in the bottom navigation
 * @property icon The icon shown in the bottom navigation
 */
enum class AppScreen(val route: String, val title: String, val icon: ImageVector) {
    HOME(Routes.Track.route, "Track", Icons.Filled.Home),
    HISTORY(Routes.History.route, "History", Icons.Filled.DateRange),
    FEED(Routes.Feed.route, "Feed", Icons.Filled.Share),
    ACCOUNT(Routes.Account.route, "Account", Icons.Filled.AccountCircle)
}

/**
 * Composable function that creates the bottom navigation bar for the application.
 * Features:
 * - Fixed bottom positioning
 * - Custom navigation bar colors
 * - Responsive navigation items with icons and labels
 * - Visual feedback for selected items
 *
 * @param navController The NavController managing app navigation
 */
@Composable
fun BottomNavBar(navController: NavController) {
    // Track the current navigation state
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        NavigationBar(
            containerColor = Color(0xFF213455)  // Dark blue theme color
        ) {
            // Create navigation items for each screen in the app
            AppScreen.entries.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute?.endsWith(item.route.toString()) == true,
                    onClick = {
                        navController.navigate(item.route.toString())
                    },
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(text = item.title) },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF101E36),    // Darker blue for selected icon
                        selectedTextColor = Color(0xFFFFFFFF),    // White for selected text
                        unselectedIconColor = Color.White,        // White for unselected icon
                        unselectedTextColor = Color.White         // White for unselected text
                    )
                )
            }
        }
    }
}