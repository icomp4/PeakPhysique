package com.peakphysique.app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.peakphysique.app.controller.BottomNavBar

@Composable
fun FeedScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 44.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
        ) {
            items(feedData) { entry ->
                FeedEntry(type = entry.type, achievement = entry.achievement, time = entry.time, icon = entry.icon)
            }
        }
    }

    BottomNavBar(navController = navController)
}

data class FeedEntryData(val type: String, val achievement: String, val time: String, val icon: ImageVector)

// Hardcoded feed data for now until we incorporate a database
val feedData = listOf(
    FeedEntryData("New Bench Press PR", "225lbs Wow! That's a 20% increase from last month!", "2 hours ago", Icons.Default.Favorite),
    FeedEntryData("Get out there and try again", "You deadlifted 205 this week, That's a 10% decrease from last month.", "3 hours ago", Icons.Default.Close),
    FeedEntryData("New Achievement", "Completed 5 workouts this week", "4 hours ago", Icons.Default.Star),
    FeedEntryData("New Achievement", "Completed 10 workouts this month", "10 hours ago", Icons.Default.Star),
    FeedEntryData("You're on fire!", "You've worked out 5 days in a row", "1 day ago", Icons.Default.Star),
    FeedEntryData("New Bench Press PR", "225lbs Wow! That's a 20% increase from last month!", "2 hours ago", Icons.Default.Favorite),
    FeedEntryData("Get out there and try again", "You deadlifted 205 this week, That's a 10% decrease from last month.", "3 hours ago", Icons.Default.Close),
    FeedEntryData("New Achievement", "Completed 5 workouts this week", "4 hours ago", Icons.Default.Star),
    FeedEntryData("New Achievement", "Completed 10 workouts this month", "10 hours ago", Icons.Default.Star),
    FeedEntryData("You're on fire!", "You've worked out 5 days in a row", "1 day ago", Icons.Default.Star),
    FeedEntryData("New Bench Press PR", "225lbs Wow! That's a 20% increase from last month!", "2 hours ago", Icons.Default.Favorite),
    FeedEntryData("Get out there and try again", "You deadlifted 205 this week, That's a 10% decrease from last month.", "3 hours ago", Icons.Default.Close),
    FeedEntryData("New Achievement", "Completed 5 workouts this week", "4 hours ago", Icons.Default.Star),
    FeedEntryData("New Achievement", "Completed 10 workouts this month", "10 hours ago", Icons.Default.Star),
    FeedEntryData("You're on fire!", "You've worked out 5 days in a row", "1 day ago", Icons.Default.Star),
    FeedEntryData("New Bench Press PR", "225lbs Wow! That's a 20% increase from last month!", "2 hours ago", Icons.Default.Favorite),
    FeedEntryData("Get out there and try again", "You deadlifted 205 this week, That's a 10% decrease from last month.", "3 hours ago", Icons.Default.Close),
    FeedEntryData("New Achievement", "Completed 5 workouts this week", "4 hours ago", Icons.Default.Star),
    FeedEntryData("New Achievement", "Completed 10 workouts this month", "10 hours ago", Icons.Default.Star),
    FeedEntryData("You're on fire!", "You've worked out 5 days in a row", "1 day ago", Icons.Default.Star),
)

@Composable
fun FeedEntry(type: String, achievement: String, time: String, icon: ImageVector, modifier: Modifier = Modifier) {
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
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF6D30F7), Color(0xFF9BF8F4))
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

            Column(
                modifier = Modifier.weight(1f)
            ) {
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
                        color = Color(0xC8030303),
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = achievement,
                    color = Color(0xC8141414),
                    fontSize = 14.sp
                )
            }
        }
    }
}
