/*
    Example view, should define the structure of the view that will be displayed to the user
    This file should be located in the view package, and only handle the UI logic
    It should also incorporate a controller to handle the business logic
 */

/**
 * RegisterScreen is a user interface component responsible for new user registration.
 * It provides a form for collecting user credentials and account creation.
 *
 * Key features:
 * - Username, email, and password input fields
 * - Account creation button
 * - Navigation link to login screen for existing users
 * - Responsive layout with centered content
 */

package com.peakphysique.app.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.peakphysique.app.ui.theme.Buttons

@Composable
fun RegisterScreen(
    navController: NavController,  // Navigation controller for handling screen transitions
    modifier: Modifier = Modifier   // Optional modifier for customizing the screen's layout
) {
    // State management for form inputs
    var username by remember { mutableStateOf("") }  // Stores username input value
    var email by remember { mutableStateOf("") }     // Stores email input value
    var password by remember { mutableStateOf("") }  // Stores password input value

    // Root column containing all screen elements
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,  // Center-align all children horizontally
        verticalArrangement = Arrangement.Center            // Center-align all children vertically
    ) {
        // App title display
        Text(
            text = "Peak Physique",
            fontSize = 32.sp,
            modifier = Modifier.padding(20.dp)
        )

        // Screen title display
        Text(
            text = "Register Screen",
            fontSize = 24.sp,
            modifier = Modifier.padding(20.dp)
        )

        // Username input field
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Enter your username...") },
            modifier = Modifier
                .width(400.dp)    // Fixed width for consistent form layout
                .padding(20.dp)   // Spacing between form elements
        )

        // Email input field
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter your email...") },
            modifier = Modifier
                .width(400.dp)
                .padding(20.dp)
        )

        // Password input field with hidden text
        TextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),  // Masks password input
            label = { Text("Enter your password...") },
            modifier = Modifier
                .width(400.dp)
                .padding(20.dp)
        )

        // Account creation button
        Button(
            onClick = {
                // TODO: Implement account creation logic
                // Should validate inputs and call appropriate authentication service
            },
            modifier = Modifier.width(150.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Buttons)  // Custom theme color
        ) {
            Text("Create Account", color = Color.White)
        }

        // Login redirect section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            // Static text
            Text(
                text = "Already have an account?",
                modifier = Modifier.padding(end = 4.dp)
            )

            // Clickable login link
            Text(
                text = "Login",
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable(onClick = { navController.navigate("login_screen") })  // Navigate to login screen
                    .padding(start = 4.dp)
            )
        }
    }
}
