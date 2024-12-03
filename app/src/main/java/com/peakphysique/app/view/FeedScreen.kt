/**
 * FeedScreen displays a social-style activity feed of user achievements and milestones.
 * It shows a scrollable list of feed entries, each containing information about
 * different types of achievements, including workout completions, personal records,
 * and other fitness milestones.
 *
 * Features:
 * - Scrollable activity feed
 * - Visual achievement cards with icons
 * - Timestamp display for each achievement
 * - Bottom navigation integration
 */
package com.peakphysique.app.view

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.peakphysique.app.controller.BottomNavBar
import com.peakphysique.app.viewmodel.FeedViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.peakphysique.app.viewmodel.FeedViewModelFactory

/**
 * Main composable for the feed screen that displays user achievements and activities.
 *
 * @param navController Navigation controller for handling screen transitions
 * @param viewModel ViewModel instance for managing feed data
 * @param modifier Optional modifier for customizing layout
 */
@Composable
fun FeedScreen(
    navController: NavController,
    viewModel: FeedViewModel = viewModel(
        factory = FeedViewModelFactory.provide(LocalContext.current.applicationContext as Application)
    ),
    modifier: Modifier = Modifier
) {
    // Collect feed entries from ViewModel as state
    val feedEntries by viewModel.feedEntries.collectAsState()

    // Main container for feed content
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 44.dp)
    ) {
        // Scrollable list of feed entries
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)  // Add padding for bottom navigation bar
        ) {
            items(feedEntries) { entry ->
                FeedEntry(
                    type = entry.type,
                    achievement = entry.achievement,
                    time = entry.time,
                    icon = entry.icon
                )
            }
        }
    }

    // Bottom navigation bar
    BottomNavBar(navController = navController)
}

/**
 * Data class representing a single feed entry with achievement details.
 *
 * @property type Type of achievement (e.g., "Workout", "Personal Record")
 * @property achievement Description of the achievement
 * @property time Timestamp of when the achievement occurred
 * @property icon Icon representing the achievement type
 */
data class FeedEntryData(
    val type: String,
    val achievement: String,
    val time: String,
    val icon: ImageVector
)

/**
 * Composable for displaying a single feed entry card.
 * Each card shows an achievement type, description, timestamp, and themed icon.
 *
 * @param type Type of achievement
 * @param achievement Description of the achievement
 * @param time Timestamp of the achievement
 * @param icon Icon representing the achievement type
 * @param modifier Optional modifier for customizing layout
 */
@Composable
fun FeedEntry(
    type: String,
    achievement: String,
    time: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular icon container with gradient background
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF6D30F7), Color(0xFF9BF8F4))  // Purple to cyan gradient
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "$type's icon",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Achievement details column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Type and timestamp row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = type,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = time,
                        color = Color(0xC8030303),  // Semi-transparent black
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                // Achievement description
                Text(
                    text = achievement,
                    color = Color(0xC8141414),  // Semi-transparent black
                    fontSize = 14.sp
                )
            }
        }
    }
}