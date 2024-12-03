package com.peakphysique.app.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.peakphysique.app.controller.BottomNavBar
import com.peakphysique.app.viewmodel.ProgressViewModel

@Composable
fun ProgressScreen(
    navController: NavController,
    viewModel: ProgressViewModel = viewModel(),
) {
    val currentWeight by viewModel.currentWeight.collectAsState()
    val startingWeight by viewModel.startingWeight.collectAsState()
    val strengthProgress by viewModel.strengthProgress.collectAsState()
    val monthlyWorkouts by viewModel.monthlyWorkouts.collectAsState()
    val monthlyPRs by viewModel.monthlyPRs.collectAsState()
    val goalWeight by viewModel.goalWeight.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = 42.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Progress Overview",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Weight Progress Card
        if (currentWeight != null && startingWeight != null) {
            WeightProgressCard(
                currentWeight = currentWeight!!,
                startingWeight = startingWeight!!,
                goalWeight = goalWeight
            )
        }

        // Strength Progress Card
        if (strengthProgress.isNotEmpty()) {
            StrengthProgressCard(strengthProgress = strengthProgress)
        }

        // Monthly Progress Card
        MonthlyProgressCard(
            workouts = monthlyWorkouts,
            prs = monthlyPRs
        )
    }
    BottomNavBar(navController = navController)
}

@Composable
private fun WeightProgressCard(
    currentWeight: Float,
    startingWeight: Float,
    goalWeight: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Weight Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeightProgressStat(
                    label = "Starting",
                    value = startingWeight,
                    icon = Icons.Default.DateRange
                )
                WeightProgressStat(
                    label = "Current",
                    value = currentWeight,
                    icon = Icons.Default.LocationOn
                )
                WeightProgressStat(
                    label = "Goal",
                    value = goalWeight,
                    icon = Icons.Default.Star
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = (startingWeight - currentWeight) / (startingWeight - goalWeight),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Text(
                text = "Total Progress: ${String.format("%.1f", (startingWeight - currentWeight))} lbs",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun StrengthProgressCard(
    strengthProgress: Map<String, Pair<Float, Float>>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Strength Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            strengthProgress.forEach { (exercise, progress) ->
                StrengthProgressItem(
                    exercise = exercise,
                    startWeight = progress.first,
                    currentWeight = progress.second
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun MonthlyProgressCard(
    workouts: Int,
    prs: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Monthly Achievements",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AchievementStat(
                    value = workouts.toString(),
                    label = "Workouts",
                    icon = Icons.Default.Build
                )
                AchievementStat(
                    value = prs.toString(),
                    label = "PRs Set",
                    icon = Icons.Default.Star
                )
            }
        }
    }
}

@Composable
private fun WeightProgressStat(
    label: String,
    value: Float,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = String.format("%.1f lbs", value),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StrengthProgressItem(
    exercise: String,
    startWeight: Float,
    currentWeight: Float
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = exercise,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${String.format("%.1f", currentWeight)} lbs",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = currentWeight / (startWeight * 1.5f),
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )

        Text(
            text = "Started at: ${String.format("%.1f", startWeight)} lbs",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AchievementStat(
    value: String,
    label: String,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}