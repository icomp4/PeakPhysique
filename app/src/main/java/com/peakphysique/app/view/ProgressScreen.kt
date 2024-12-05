package com.peakphysique.app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.peakphysique.app.controller.BottomNavBar
import com.peakphysique.app.viewmodel.ProgressViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                modifier = Modifier.height(48.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Progress Overview",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            if (currentWeight != null && startingWeight != null) {
                WeightProgressCard(
                    currentWeight = currentWeight!!,
                    startingWeight = startingWeight!!,
                    goalWeight = goalWeight
                )
            }

            if (strengthProgress.isNotEmpty()) {
                StrengthProgressCard(strengthProgress = strengthProgress)
            }

            MonthlyProgressCard(
                workouts = monthlyWorkouts,
                prs = monthlyPRs
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
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
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            CardHeader(title = "Weight Progress", icon = Icons.Default.Face)

            Spacer(modifier = Modifier.height(24.dp))

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

            Spacer(modifier = Modifier.height(24.dp))

            LinearProgressIndicator(
                progress = (startingWeight - currentWeight) / (startingWeight - goalWeight),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer
            )

            Text(
                text = "Total Progress: ${String.format("%.1f", (startingWeight - currentWeight))} lbs",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
private fun StrengthProgressCard(
    strengthProgress: Map<String, Pair<Float, Float>>,
    modifier: Modifier = Modifier
) {
    val viewModel: ProgressViewModel = viewModel()
    val goals by viewModel.strengthGoals.collectAsState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            CardHeader(title = "Strength Progress", icon = Icons.Default.Favorite)

            Spacer(modifier = Modifier.height(24.dp))

            strengthProgress.forEach { (exercise, progress) ->
                val goalWeight = when (exercise.lowercase()) {
                    "bench press" -> goals.benchGoal
                    "squat" -> goals.squatGoal
                    "deadlift" -> goals.deadliftGoal
                    else -> progress.first * 1.5f // fallback for other exercises
                }

                StrengthProgressItem(
                    exercise = exercise,
                    startWeight = progress.first,
                    currentWeight = progress.second,
                    goalWeight = goalWeight
                )
                if (exercise != strengthProgress.keys.last()) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
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
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            CardHeader(title = "Monthly Achievements", icon = Icons.Default.Star)

            Spacer(modifier = Modifier.height(24.dp))

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
private fun CardHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
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
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = String.format("%.1f lbs", value),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StrengthProgressItem(
    exercise: String,
    startWeight: Float,
    currentWeight: Float,
    goalWeight: Float
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = exercise,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${String.format("%.1f", currentWeight)} lbs",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = (currentWeight - startWeight) / (goalWeight - startWeight).coerceAtLeast(1f),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primaryContainer
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Started: ${String.format("%.1f", startWeight)} lbs",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Goal: ${String.format("%.1f", goalWeight)} lbs",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
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
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}