package com.peakphysique.app.view

import TrackingViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.peakphysique.app.controller.BottomNavBar
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.YearMonth

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: TrackingViewModel = viewModel()
) {
    val selectedDay = remember { mutableStateOf(LocalDate.now().dayOfMonth) }
    val selectedMonth = remember { mutableStateOf(LocalDate.now().monthValue) }
    val selectedYear = remember { mutableStateOf(LocalDate.now().year) }

    // Collect all workouts from the repository
    val allWorkouts by viewModel.allWorkouts.collectAsState()

    // Transform WorkoutWithSets to calendar data by extracting dates
    val workoutDates = allWorkouts
        .map { it.workout.date.toLocalDate() }  // Convert LocalDateTime to LocalDate
        .filter { date ->  // Filter dates for the selected month/year
            date.year == selectedYear.value &&
                    date.monthValue == selectedMonth.value
        }
        .map { it.dayOfMonth }
        .distinct()

    // Filter workouts for selected date
    val selectedDateWorkouts = allWorkouts.filter { workout ->
        val workoutDate = workout.workout.date
        workoutDate.year == selectedYear.value &&
                workoutDate.monthValue == selectedMonth.value &&
                workoutDate.dayOfMonth == selectedDay.value
    }

    // Transform WorkoutWithSets to WorkoutData for display
    val workoutDisplayData = selectedDateWorkouts.flatMap { workoutWithSets ->
        workoutWithSets.sets.groupBy { it.name }.map { (exerciseName, sets) ->
            WorkoutData(
                exerciseName = exerciseName,
                sets = sets.size,
                reps = sets.firstOrNull()?.reps?.toIntOrNull() ?: 0,
                weight = sets.firstOrNull()?.weight?.toDoubleOrNull() ?: 0.0
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Month navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (selectedMonth.value == 1) {
                        selectedMonth.value = 12
                        selectedYear.value -= 1
                    } else {
                        selectedMonth.value -= 1
                    }
                    // Reset selected day when changing months
                    selectedDay.value = 1
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF213455),
                    contentColor = Color.White
                )
            ) {
                Text("<")
            }

            Text(
                text = "${Month.of(selectedMonth.value).name.lowercase().capitalize()} - ${selectedYear.value}",
            )

            Button(
                onClick = {
                    if (selectedMonth.value == 12) {
                        selectedMonth.value = 1
                        selectedYear.value += 1
                    } else {
                        selectedMonth.value += 1
                    }
                    // Reset selected day when changing months
                    selectedDay.value = 1
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF213455),
                    contentColor = Color.White
                )
            ) {
                Text(">")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Calendar view
        CalendarView(
            year = selectedYear.value,
            month = selectedMonth.value,
            workoutDates = workoutDates,
            selectedDay = selectedDay.value,
            currentDay = if (selectedYear.value == LocalDate.now().year &&
                selectedMonth.value == LocalDate.now().monthValue)
                LocalDate.now().dayOfMonth else -1,
            onDateClick = { day ->
                selectedDay.value = day
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (workoutDisplayData.isEmpty() && selectedDay.value > 0) {
            Text(
                text = "No workouts recorded for this date",
                modifier = Modifier.padding(16.dp),
                color = Color.Gray
            )
        }

        // Workout cards
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(workoutDisplayData) { workout ->
                WorkoutCard(workout = workout)
            }
        }
    }
    BottomNavBar(navController)
}

// Add this extension function to support date conversions
fun LocalDateTime.toLocalDate(): LocalDate {
    return LocalDate.of(this.year, this.month, this.dayOfMonth)
}

@Composable
fun WorkoutCard(workout: WorkoutData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = workout.exerciseName,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Sets: ${workout.sets}")
                    Text(text = "Reps: ${workout.reps}")
                }
                Column {
                    Text(text = "Weight: ${workout.weight}lbs")
                }
            }
        }
    }
}

@Composable
fun WeekHeader() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
            Text(text = day, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun CalendarView(
    year: Int,
    month: Int,
    workoutDates: List<Int>,
    selectedDay: Int,
    currentDay: Int,
    onDateClick: (Int) -> Unit
) {
    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
    val firstDayOfWeek = LocalDate.of(year, month, 1).dayOfWeek.value % 7

    Column {
        WeekHeader()
        LazyVerticalGrid(columns = GridCells.Fixed(7)) {
            // Add empty cells for the first day offset
            repeat(firstDayOfWeek) {
                item { Spacer(modifier = Modifier.size(40.dp)) }
            }

            // Add cells for each day of the month
            for (day in 1..daysInMonth) {
                item {
                    DayCell(
                        day = day,
                        isWorkoutDay = day in workoutDates,
                        isSelected = day == selectedDay,
                        currentDay = day == currentDay,
                        onClick = { onDateClick(day) }
                    )
                }
            }
        }
    }
}

@Composable
fun DayCell(
    day: Int,
    isWorkoutDay: Boolean,
    isSelected: Boolean,
    currentDay: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(4.dp)
            .clip(CircleShape)
            .background(when {
                isSelected -> Color(0xFF003D6E)
                isWorkoutDay -> Color(0x8A7CA8FF)
                currentDay -> Color(0xFF969696)
                else -> Color.Transparent
            })
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            color = if (isSelected) Color.White else Color.Black,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = if (currentDay) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}

data class WorkoutData(
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val weight: Double,
)