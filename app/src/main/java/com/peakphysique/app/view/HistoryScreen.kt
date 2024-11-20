package com.peakphysique.app.view

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.peakphysique.app.controller.BottomNavBar
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

/*
    TODO 1 : Make calendar look nicer
    TODO 2 : Remove Today's date and selected date at top of screen
    TODO 3 : When button is clicked to go to next/previous month,
             deselect the day pressed
 */
@Composable
fun HistoryScreen(navController: NavController) {
    // Existing state for date selection
    val selectedDay = remember { mutableStateOf(LocalDate.now().dayOfMonth) }
    val selectedMonth = remember { mutableStateOf(LocalDate.now().monthValue) }
    val selectedYear = remember { mutableStateOf(LocalDate.now().year) }

    // New state for workout data
    val workouts = remember {
        mutableStateOf(listOf<WorkoutData>())
    }

    // Get the current date for the header
    val currentDate = LocalDate.now()
    val currentDateText = "${currentDate.year}-${currentDate.monthValue}-${currentDate.dayOfMonth}"
    val selectedDateText = "${selectedYear.value}-${selectedMonth.value}-${selectedDay.value}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display current and selected dates
        Text(text = "Today's Date: $currentDateText")
        Text(text = "Selected Date: $selectedDateText")

        Spacer(modifier = Modifier.height(16.dp))

        // Month navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                if (selectedMonth.value == 1) {
                    selectedMonth.value = 12
                    selectedYear.value -= 1
                } else {
                    selectedMonth.value -= 1
                }
            }) {
                Text("<")
            }

            Text(
                text = "${Month.of(selectedMonth.value).name.lowercase().capitalize()} - ${selectedYear.value}",
            )

            Button(onClick = {
                if (selectedMonth.value == 12) {
                    selectedMonth.value = 1
                    selectedYear.value += 1
                } else {
                    selectedMonth.value += 1
                }
            }) {
                Text(">")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display the calendar
        CalendarView(
            year = selectedYear.value,
            month = selectedMonth.value,
            workoutDates = listOf(2, 3), // Replace with actual workout dates
            selectedDay = selectedDay.value,
            currentDay = if (selectedYear.value == currentDate.year && selectedMonth.value == currentDate.monthValue) currentDate.dayOfMonth else -1,
            onDateClick = { day ->
                selectedDay.value = day
                // Here you would fetch workouts for the selected date
                workouts.value = getWorkoutsForDate(selectedYear.value, selectedMonth.value, day)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display workout cards for selected date
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(workouts.value) { workout ->
                WorkoutCard(workout = workout)
            }
        }
    }
    BottomNavBar(navController)
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
                    Text(
                        text = "Sets: ${workout.sets}",
                    )
                    Text(
                        text = "Reps: ${workout.reps}",
                    )
                }
                Column {
                    Text(
                        text = "Weight: ${workout.weight}lbs",
                    )
                }
            }
        }
    }
}

// Data class for workout information
data class WorkoutData(
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val weight: Double,
)

// Function to get workouts for a specific date
fun getWorkoutsForDate(year: Int, month: Int, day: Int): List<WorkoutData> {
    // Replace this with actual data
    // This is just a sample implementation
    return if (day % 2 == 0) {
        listOf(
            WorkoutData(
                exerciseName = "Bench Press",
                sets = 3,
                reps = 10,
                weight = 255.0,
            ),
            WorkoutData(
                exerciseName = "Squats",
                sets = 4,
                reps = 8,
                weight = 315.0,
            )
        )
    } else {
        listOf(
            WorkoutData(
                exerciseName = "Deadlift",
                sets = 3,
                reps = 5,
                weight = 500.0,
            )
        )
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
    workoutDates: List<Int>,  // List of days with workouts
    selectedDay: Int, // Day of the month selected by user
    currentDay : Int, // Current day of the month
    onDateClick: (Int) -> Unit // Callback for date selection
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
    onClick: () -> Unit,
    currentDay: Boolean
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(4.dp)  // Add padding to create space between cells
            .clip(CircleShape)  // Clip the background to a circle
            .background(when {
                isSelected -> Color(0xFF003D6E) // Navy blue for selected date
                isWorkoutDay -> Color(0x8A7CA8FF)      // Light blue for workout days
                currentDay -> Color(0xFF969696) // Current day is gray
                else -> Color.Transparent        // Transparent for empty days
            })
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            color = if (isSelected) Color.White else Color.Black,  // Make text white when selected for better contrast
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = if (currentDay) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}
