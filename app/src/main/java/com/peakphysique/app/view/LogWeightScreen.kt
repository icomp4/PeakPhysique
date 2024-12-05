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
import com.peakphysique.app.viewmodel.WeightViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun LogWeightScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: WeightViewModel = viewModel()
) {
    var weight by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    val latestWeight by viewModel.latestWeight.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Latest Weight Display
        latestWeight?.let {
            Text(
                text = "Latest Weight: $it lbs",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Weight Input Field
        OutlinedTextField(
            value = weight,
            onValueChange = {
                weight = it
                showError = false
                showSuccess = false
            },
            label = { Text("Weight (lbs)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = showError,
            supportingText = when {
                showError -> { { Text("Please enter a valid weight") } }
                showSuccess -> { { Text("Weight logged successfully!") } }
                else -> null
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val weightValue = weight.toFloatOrNull()
                if (weightValue != null && weightValue > 0) {
                    viewModel.logWeight(weightValue)
                    weight = ""
                    showError = false
                    showSuccess = true
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

        Text(
            text = "Date: ${LocalDateTime.now().toLocalDate()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    BottomNavBar(navController = navController)
}