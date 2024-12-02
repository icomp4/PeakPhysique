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
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var weight by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        // Weight Input Field
        OutlinedTextField(
            value = weight,
            onValueChange = {
                weight = it
                showError = false
            },
            label = { Text("Weight (lbs)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = showError,
            supportingText = if (showError) {
                { Text("Please enter a valid weight") }
            } else null,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Log Button
        Button(
            onClick = {
                val weightValue = weight.toFloatOrNull()
                if (weightValue != null && weightValue > 0) {
                    weight = ""
                    showError = false
                } else {
                    showError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Log Weight")
        }

        // Today's date
        Text(
            text = "Date: ${LocalDateTime.now().toLocalDate()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

    }
    BottomNavBar(navController = navController)

}