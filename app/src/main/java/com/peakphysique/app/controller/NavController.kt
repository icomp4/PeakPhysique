package com.peakphysique.app.controller

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peakphysique.app.view.LoginScreen
import com.peakphysique.app.view.RegisterScreen

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
    }
}