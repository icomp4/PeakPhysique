/**
 * LogWeightScreen provides a user interface for logging daily weight measurements.
 * It features a simple form with weight input and validation, displaying the current date
 * for record-keeping purposes.
 *
 * Key features:
 * - Decimal weight input with validation
 * - Error handling for invalid inputs
 * - Current date display
 * - Integration with bottom navigation
 */
package com.peakphysique.app.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import androidx.navigation.NavController
import com.peakphysique.app.controller.BottomNavBar

@Composable
fun LogWeightScreen(
    navController: NavController,     // For handling navigation between screens
    modifier: Modifier = Modifier     // Optional modifier for customizing layout
) {
    // State management
    var weight by remember { mutableStateOf("") }        // Stores the weight input value
    var showError by remember { mutableStateOf(false) }  // Controls error message visibility

    // Main container with padding and arrangement
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)  // Consistent spacing between elements
    ) {
        // Top spacing for visual balance
        Spacer(modifier = Modifier.height(24.dp))

        // Weight input field with validation
        OutlinedTextField(
            value = weight,
            onValueChange = {
                weight = it
                showError = false  // Reset error state when user types
            },
            label = { Text("Weight (lbs)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal  // Show decimal keyboard for weight input
            ),
            isError = showError,
            supportingText = if (showError) {
                { Text("Please enter a valid weight") }  // Error message when input is invalid
            } else null,
            modifier = Modifier.fillMaxWidth()
        )

        // Spacing between input field and button
        Spacer(modifier = Modifier.height(24.dp))

        // Log weight button with validation logic
        Button(
            onClick = {
                val weightValue = weight.toFloatOrNull()
                // Validate weight is a positive number
                if (weightValue != null && weightValue > 0) {
                    // TODO: Implement weight logging to data store
                    weight = ""        // Reset input field
                    showError = false  // Clear any error state
                } else {
                    showError = true   // Show error for invalid input
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)  // Increased touch target for better accessibility
        ) {
            Text("Log Weight")
        }

        // Current date display for weight entry reference
        Text(
            text = "Date: ${LocalDateTime.now().toLocalDate()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    // Bottom navigation bar fixed at bottom of screen
    BottomNavBar(navController = navController)
}