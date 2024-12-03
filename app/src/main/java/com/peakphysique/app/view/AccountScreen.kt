/**
 * AccountScreen serves as the user's profile page within the Peak Physique app.
 * It displays user information, statistics, and provides navigation to various account-related features.
 *
 * The screen is divided into three main sections:
 * 1. Profile Card - User's basic information and profile picture
 * 2. Statistics Card - Quick overview of user's achievements
 * 3. Navigation Menu - Links to various account-related features
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

// TODO: Pull user data from database - Implement user data retrieval from backend

@Composable
fun AccountScreen(
    navController: NavController,
    onEditProfile: () -> Unit = {},           // Callback for profile editing
    onViewWorkoutHistory: () -> Unit = {},    // Callback for viewing workout history
    onSettings: () -> Unit = {},              // Callback for settings navigation
    onLogout: () -> Unit = {}                 // Callback for logout action
) {
    // State for handling scrollable content
    val scrollState = rememberScrollState()

    // Main container for all content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Card Section
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Profile Picture placeholder
                    Surface(
                        modifier = Modifier
                            .padding(top = 34.dp)
                            .size(100.dp),
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 1.dp
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .padding(16.dp)
                                .size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // User information display
                    Text(
                        text = "John Doe",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Member since: Jan 2024",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Profile edit button
                    Button(
                        onClick = onEditProfile,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Edit Profile")
                    }
                }
            }
        }

        // Statistics Card Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Quick Stats",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Stats row displaying user achievements
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(number = "98", label = "Workouts")
                    StatItem(number = "12", label = "Records Broken")
                    StatItem(number = "6", label = "Months Active")
                }
            }
        }

        // Navigation Menu Items
        MenuListItem(
            icon = Icons.Default.FavoriteBorder,
            title = "View Progress",
            onClick = { navController.navigate("progress_screen") }
        )

        MenuListItem(
            icon = Icons.Default.DateRange,
            title = "Workout History",
            onClick = onViewWorkoutHistory
        )

        MenuListItem(
            icon = Icons.Default.Add,
            title = "Log Weight",
            onClick = { navController.navigate("log_weight_screen") }
        )

        MenuListItem(
            icon = Icons.Default.Settings,
            title = "Settings",
            onClick = onSettings
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Logout button
        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Logout")
        }
    }
    BottomNavBar(navController = navController)
}

/**
 * Displays a single statistic item with a number and label.
 *
 * @param number The numerical value to display
 * @param label Description of what the number represents
 */
@Composable
private fun StatItem(
    number: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = number,
            style = MaterialTheme.typography.titleLarge,
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
 * Creates a clickable menu item with an icon, title, and arrow indicator.
 *
 * @param icon Vector icon to display on the left
 * @param title Text label for the menu item
 * @param onClick Callback for when the item is clicked
 */
@Composable
private fun MenuListItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left icon
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Menu item title
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            // Right arrow indicator
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}