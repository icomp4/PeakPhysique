package com.peakphysique.app.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.peakphysique.app.controller.BottomNavBar
import com.peakphysique.app.viewmodel.AccountViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.peakphysique.app.R

@Composable
fun AccountScreen(
    navController: NavController,
    viewModel: AccountViewModel = viewModel(),
    onViewWorkoutHistory: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val totalWorkouts by viewModel.totalWorkouts.collectAsState()
    val recordsBroken by viewModel.recordsBroken.collectAsState()
    val monthsActive by viewModel.monthsActive.collectAsState()
    val displayName by viewModel.displayName.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Section
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

                // Profile picture
                Image(
                    painter = painterResource(id = R.drawable.logo_),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .padding(top = 34.dp)
                        .size(200.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = displayName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Member since: Jan 2024",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

        // Stats Section
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(
                        number = totalWorkouts.toString(),
                        label = "Workouts"
                    )
                    StatItem(
                        number = recordsBroken.toString(),
                        label = "Records Broken"
                    )
                    StatItem(
                        number = monthsActive.toString(),
                        label = "Months Active"
                    )
                }
            }
        }

        // Menu Items
        MenuListItem(
            icon = Icons.Default.FavoriteBorder,
            title = "View Progress",
            onClick = { navController.navigate("progress_screen") }
        )


        MenuListItem(
            icon = Icons.Default.Add,
            title = "Log Weight",
            onClick = { navController.navigate("log_weight_screen") }
        )

        MenuListItem(
            icon = Icons.Default.Settings,
            title = "Settings",
            onClick = { navController.navigate("settings_screen") }
        )

        Spacer(modifier = Modifier.height(16.dp))

    }
    BottomNavBar(navController = navController)
}

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
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}