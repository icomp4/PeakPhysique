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

sealed class Routes {
    data object Track
    data object History
    data object Feed
    data object Account
}

enum class AppScreen(val route: Any, val title: String, val icon: ImageVector) {

    // TODO: Make sure to add the correct routes
    HOME(Routes.Track, "Track", Icons.Filled.Home),
    HISTORY(Routes.History, "History", Icons.Filled.DateRange),
    FEED(Routes.Feed, "Feed", Icons.Filled.Share),
    ACCOUNT(Routes.Account, "Account", Icons.Filled.AccountCircle)
}

@Composable
fun BottomNavBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        NavigationBar(
            containerColor = Color(0xFF213455)
        ) {
            AppScreen.values().forEach { item ->
                NavigationBarItem(
                    selected = currentRoute?.endsWith(item.route.toString()) == true,
                    onClick = { // TODO: add navigation logic
                    },
                    icon = { Icon(item.icon, contentDescription = item.title, tint = Color.White) },
                    label = { Text(text = item.title, color = Color.White) },
                    alwaysShowLabel = true
                )
            }
        }
    }
}
