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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.YearMonth


@Composable
fun HistoryScreen(navController: NavController) {
    // State to track the selected day
    val selectedDay = remember { mutableStateOf(LocalDate.now().dayOfMonth) }
    val selectedMonth = remember { mutableStateOf(LocalDate.now().monthValue) }
    val selectedYear = remember { mutableStateOf(LocalDate.now().year) }

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

        // Display the calendar at the top
        CalendarView(
            year = selectedYear.value,
            month = selectedMonth.value,
            workoutDates = listOf(currentDate.dayOfMonth), // Replace with your workout dates
            selectedDay = selectedDay.value,
            onDateClick = { day -> selectedDay.value = day // Update selected day when a date is clicked
             },

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
    selectedDay: Int,
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
                        onClick = { onDateClick(day) }
                    )
                }
            }
        }
    }
}
@Composable
fun DayCell(day: Int, isWorkoutDay: Boolean, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clickable(onClick = onClick)
            .background(when {
                isSelected -> Color(0xFF003D6E) // Navy blue for selected date
                isWorkoutDay -> Color(0xFF969696)      // Black for workout days
                else -> Color.Transparent        // Transparent for empty days
            }),
        contentAlignment = Alignment.Center
    ) {
        Text(text = day.toString(), color = Color.Black)
    }
}
/*@Composable
fun WorkoutDetails(day: Int, workoutList: List<Workout>) {
    Column {
        workoutList.forEach { workout ->
            Card(
                modifier = Modifier.padding(8.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Exercise: ${workout.name}")
                    Text(text = "Reps: ${workout.reps}")
                    Text(text = "Weight: ${workout.weight}")
                    Text(text = "Notes: ${workout.notes}")
                }
            }
        }
    }
}

*/