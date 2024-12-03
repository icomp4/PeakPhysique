/**
 * LoginScreen provides the user authentication interface for the Peak Physique app.
 * It allows existing users to log in using their email/username and password,
 * with navigation options to the registration screen for new users.
 *
 * Key features:
 * - Email/username input field
 * - Secure password input with masked text
 * - Navigation to main feed upon successful login
 * - Registration link for new users
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
import androidx.navigation.NavHostController
import com.peakphysique.app.ui.theme.Buttons

@Composable
fun LoginScreen(
    navController: NavController,     // For handling navigation between screens
    modifier: Modifier = Modifier     // Optional modifier for customizing layout
) {
    // State management for form inputs
    var username by remember { mutableStateOf("") }  // Unused in current implementation
    var email by remember { mutableStateOf("") }     // Stores email/username input
    var password by remember { mutableStateOf("") }  // Stores password input

    // Main container for login form
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,  // Center align all children
        verticalArrangement = Arrangement.Center            // Vertically center content
    ) {
        // App title
        Text(
            text = "Peak Physique",
            fontSize = 32.sp,
            modifier = Modifier.padding(20.dp)
        )

        // Screen title
        Text(
            text = "Login Screen",
            fontSize = 24.sp,
            modifier = Modifier.padding(20.dp)
        )

        // Email/Username input field
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter your email/username...") },
            modifier = Modifier
                .width(400.dp)    // Fixed width for consistent form layout
                .padding(20.dp)
        )

        // Password input field with masked text
        TextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),  // Masks password input
            label = { Text("Enter your password...") },
            modifier = Modifier
                .width(400.dp)
                .padding(20.dp)
        )

        // Login button with custom styling
        Button(
            onClick = {
                // TODO: Implement user authentication
                // - Validate input fields
                // - Check credentials against database
                // - Handle authentication errors
                navController.navigate("feed_screen")  // Navigate to main feed on success
            },
            modifier = Modifier.width(150.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Buttons)  // Custom theme color
        ) {
            Text("Login", color = Color.White)
        }

        // Registration link section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            // Static text
            Text(
                text = "Don't have an account?",
                modifier = Modifier.padding(end = 4.dp)
            )

            // Clickable registration link
            Text(
                text = "Register",
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable(onClick = { navController.navigate("register_screen") })
                    .padding(start = 4.dp)
            )
        }
    }
}