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

sealed class Routes(val route: String) {
    data object Track : Routes("tracking_screen")
    data object History : Routes("history_screen")
    data object Feed : Routes("feed_screen")
    data object Account : Routes("account_screen")
}

enum class AppScreen(val route: String, val title: String, val icon: ImageVector) {
    HOME(Routes.Track.route, "Track", Icons.Filled.Home),
    HISTORY(Routes.History.route, "History", Icons.Filled.DateRange),
    FEED(Routes.Feed.route, "Feed", Icons.Filled.Share),
    ACCOUNT(Routes.Account.route, "Account", Icons.Filled.AccountCircle)
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
            AppScreen.entries.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute?.endsWith(item.route.toString()) == true,
                    onClick = { navController.navigate(item.route.toString())
                    },
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(text = item.title) },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF101E36),
                        selectedTextColor = Color(0xFFFFFFFF),
                        unselectedIconColor = Color.White,
                        unselectedTextColor = Color.White
                    )
                )
            }
        }
    }
}
