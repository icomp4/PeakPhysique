package com.peakphysique.app.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.peakphysique.app.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.peakphysique.app.viewmodel.SurveyViewModel

@Composable
fun FitnessSurveyScreen(
    navController: NavHostController,
    viewModel: SurveyViewModel = viewModel()
) {
    val displayName by viewModel.displayName.collectAsState()
    val goals by viewModel.goals.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 32.dp)
        )

        Column(
            modifier = Modifier.width(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = displayName,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = if (goals.weightGoal > 0f) goals.weightGoal.toString() else "",
                onValueChange = {
                    it.toFloatOrNull()?.let { weight -> viewModel.updateGoals(weightGoal = weight) }
                },
                label = { Text("Goal Weight (lbs)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = if (goals.squatGoal > 0f) goals.squatGoal.toString() else "",
                onValueChange = {
                    it.toFloatOrNull()?.let { weight -> viewModel.updateGoals(squatGoal = weight) }
                },
                label = { Text("Goal Squat (lbs)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = if (goals.benchGoal > 0f) goals.benchGoal.toString() else "",
                onValueChange = {
                    it.toFloatOrNull()?.let { weight -> viewModel.updateGoals(benchGoal = weight) }
                },
                label = { Text("Goal Bench Press (lbs)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = if (goals.deadliftGoal > 0f) goals.deadliftGoal.toString() else "",
                onValueChange = {
                    it.toFloatOrNull()?.let { weight -> viewModel.updateGoals(deadliftGoal = weight) }
                },
                label = { Text("Goal Deadlift (lbs)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.saveSurveyData()
                    navController.navigate("feed_screen")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Continue")
            }
        }
    }
}