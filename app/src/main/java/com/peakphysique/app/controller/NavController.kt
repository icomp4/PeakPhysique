package com.peakphysique.app.controller

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peakphysique.app.view.FeedScreen
import com.peakphysique.app.view.LoginScreen
import com.peakphysique.app.view.RegisterScreen
import com.peakphysique.app.view.TrackingScreen

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login_screen"){

        composable("login_screen"){
            LoginScreen(navController)
        }

        composable("register_screen"){
            RegisterScreen(navController)
        }
        composable("tracking_screen"){
            TrackingScreen(navController)
        }
        composable("feed_screen"){
            FeedScreen(navController)
        }
    }
}