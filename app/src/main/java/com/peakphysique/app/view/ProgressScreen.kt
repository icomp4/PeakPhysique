/**
 * ProgressScreen displays a comprehensive overview of a user's fitness journey and achievements.
 * It shows weight tracking, strength improvements, and monthly statistics in a scrollable dashboard format.
 *
 * The screen is divided into three main cards:
 * 1. Weight Progress - Shows starting, current, and goal weights with a progress indicator
 * 2. Strength Progress - Displays progress for major lifts (bench press, squat, deadlift)
 * 3. Monthly Achievements - Highlights workout consistency and personal records
 */
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
import androidx.navigation.NavController
import com.peakphysique.app.controller.BottomNavBar

@Composable
fun ProgressScreen(
    navController: NavController,
    currentWeight: Float = 105.5f,      // User's current weight
    startingWeight: Float = 280.0f,     // Initial weight when starting the program
    goalWeight: Float = 170.0f,         // Target weight to achieve
    strengthProgress: Map<String, Pair<Float, Float>> = mapOf(  // Map of exercise name to (starting weight, current weight)
        "Bench Press" to Pair(45f, 200f),
        "Squat" to Pair(225f, 315f),
        "Deadlift" to Pair(225f, 450f)
    )
) {
    // State for handling scrollable content
    val scrollState = rememberScrollState()

    // Main container for all content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = 42.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Screen title
        Text(
            text = "Progress Overview",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Weight Progress Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Weight Progress",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Weight statistics row showing starting, current, and goal weights
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

                // Progress bar showing overall weight loss progress
                LinearProgressIndicator(
                    progress = (startingWeight - currentWeight) / (startingWeight - goalWeight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )

                // Total weight loss display
                Text(
                    text = "Total Progress: ${String.format("%.1f", (startingWeight - currentWeight))} lbs",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // Strength Progress Section - Shows progress for each major lift
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Strength Progress",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Display progress for each exercise
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

        // Monthly Achievements Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Monthly Achievements",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Achievement statistics row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AchievementStat(
                        value = "12",
                        label = "Workouts",
                        icon = Icons.Default.Build
                    )
                    AchievementStat(
                        value = "3",
                        label = "PRs Set",
                        icon = Icons.Default.Star
                    )
                }
            }
        }
    }
    // Bottom navigation bar
    BottomNavBar(navController = navController)
}

/**
 * Displays a single weight-related statistic with an icon, value, and label.
 * Used in the weight progress card to show starting, current, and goal weights.
 *
 * @param label Description of the weight value (e.g., "Starting", "Current", "Goal")
 * @param value The weight value to display
 * @param icon Icon to show above the weight value
 */
@Composable
private fun WeightProgressStat(
    label: String,
    value: Float,
    icon: ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

/**
 * Displays progress information for a specific strength exercise.
 * Shows the exercise name, current weight, progress bar, and starting weight.
 *
 * @param exercise Name of the exercise
 * @param startWeight Initial weight lifted for this exercise
 * @param currentWeight Current weight being lifted
 */
@Composable
private fun StrengthProgressItem(
    exercise: String,
    startWeight: Float,
    currentWeight: Float
) {
    Column(modifier = Modifier.fillMaxWidth()) {
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

        // Progress bar showing strength improvement
        // Uses 1.5x starting weight as the target to show relative progress
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

/**
 * Displays a single achievement statistic with an icon, value, and label.
 * Used in the monthly achievements card to show various metrics.
 *
 * @param value The achievement value to display
 * @param label Description of the achievement
 * @param icon Icon representing the achievement type
 */
@Composable
private fun AchievementStat(
    value: String,
    label: String,
    icon: ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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