package com.peakphysique.app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.peakphysique.app.controller.BottomNavBar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

/*

    TODO: Add the workout to database

 */
@Composable
fun TrackingScreen(navController: NavHostController) {
    // Existing state
    var exerciseType by remember { mutableStateOf("Select Exercise") }
    val exerciseOptions = listOf("Squat", "Bench Press", "Deadlift")
    var expanded by remember { mutableStateOf(false) }
    var cardList by remember { mutableStateOf(listOf<Set>()) }
    var reps by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    val maxNoteLength = 300

    // New state for workout completion
    var showWorkoutCompleteDialog by remember { mutableStateOf(false) }

    // State for delete confirmation
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var setToDelete by remember { mutableStateOf<Set?>(null) }

    // Validation state
    val isValidInput = remember(exerciseType, reps, weight) {
        exerciseType != "Select Exercise" &&
                reps.isNotBlank() && reps.toDoubleOrNull() != null &&
                weight.isNotBlank() && weight.toDoubleOrNull() != null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp)) // Add spacing at the top

        Box {
            // Display and handle dropdown menu for selecting exercise type
            Text(
                text = exerciseType,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                exerciseOptions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            exerciseType = option
                            expanded = false
                        },
                        text = { Text(option) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Space between dropdown and input fields

        // Input field for reps with validation for numeric input
        TextField(
            value = reps,
            onValueChange = { reps = it },
            label = { Text("Reps") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = reps.isNotBlank() && reps.toDoubleOrNull() == null
        )

        Spacer(modifier = Modifier.height(16.dp)) // Space between input fields

        // Input field for weight with validation for numeric input
        TextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = weight.isNotBlank() && weight.toDoubleOrNull() == null
        )

        Spacer(modifier = Modifier.height(16.dp)) // Space between input fields

        Column(modifier = Modifier.fillMaxWidth()) {
            // Input field for notes with character limit
            TextField(
                value = notes,
                onValueChange = {
                    if (it.length <= maxNoteLength) {
                        notes = it
                    }
                },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text(
                        text = "${notes.length}/$maxNoteLength",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp)) // Space before action buttons

        // Button to add a new set to the workout
        Button(
            onClick = {
                val exercise = Set(
                    id = UUID.randomUUID().toString(),
                    name = exerciseType,
                    reps = reps,
                    weight = weight,
                    notes = notes
                )
                cardList = cardList + exercise
                reps = ""
                weight = ""
                notes = ""
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isValidInput
        ) {
            Text("Add Set")
        }

        Spacer(modifier = Modifier.height(16.dp)) // Space between buttons

        if (cardList.isNotEmpty()) {
            // Button to complete the workout
            Button(
                onClick = { showWorkoutCompleteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Complete Workout")
            }

            Spacer(modifier = Modifier.height(16.dp)) // Space before the list of sets
        }

        // Display the list of sets using LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(
                items = cardList,
                key = { it.id }
            ) { set ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            // Display set details
                            Text("Exercise: ${set.name}")
                            Text("Reps: ${set.reps}")
                            Text("Weight: ${set.weight}")
                            if (set.notes.isNotBlank()) {
                                Text("Notes: ${set.notes}")
                            }
                        }

                        // Button to delete a specific set
                        IconButton(
                            onClick = {
                                setToDelete = set
                                showDeleteConfirmation = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete set",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }

// Dialog to confirm workout completion
    if (showWorkoutCompleteDialog) {
        AlertDialog(
            onDismissRequest = { showWorkoutCompleteDialog = false },
            title = { Text("Complete Workout") },
            text = {
                Column {
                    Text("Are you sure you want to complete this workout?")
                    Text(
                        "Date: ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))}",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        "Total Sets: ${cardList.size}",
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Save the workout details and reset state
                        val workout = Workout(
                            id = UUID.randomUUID().toString(),
                            date = LocalDateTime.now(),
                            sets = cardList
                        )
                        println("Workout completed: $workout")
                        cardList = listOf()
                        showWorkoutCompleteDialog = false
                    }
                ) {
                    Text("Complete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showWorkoutCompleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

// Dialog to confirm set deletion
    if (showDeleteConfirmation && setToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteConfirmation = false
                setToDelete = null
            },
            title = { Text("Delete Set") },
            text = { Text("Are you sure you want to delete this set? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        cardList = cardList.filterNot { it.id == setToDelete?.id }
                        showDeleteConfirmation = false
                        setToDelete = null
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        setToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

// Bottom navigation bar
    BottomNavBar(navController)
}

// Data class for each set
data class Set(
    val id: String,
    val name: String,
    val reps: String,
    val weight: String,
    val notes: String
)

// Data class for the complete workout
data class Workout(
    val id: String,
    val date: LocalDateTime,
    val sets: List<Set>
)
