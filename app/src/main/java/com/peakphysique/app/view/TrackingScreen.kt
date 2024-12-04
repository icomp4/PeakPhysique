package com.peakphysique.app.view

import TrackingViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.peakphysique.app.controller.BottomNavBar
import com.peakphysique.app.model.WorkoutSet
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Composable
fun TrackingScreen(
    navController: NavHostController,
    viewModel: TrackingViewModel = viewModel()
) {
    // UI State
    var exerciseType by remember { mutableStateOf("Select Exercise") }
    val arm = "\uD83D\uDCAA"
    val leg = "\uD83E\uDDB5"

    val exerciseOptions = listOf("Squat $arm", "Bench Press $arm$leg", "Deadlift $arm$leg", "Lateral Raise $arm",
                                 "Leg Press $leg", "Overhead Press $arm", "Barbell Curls $arm", "Hammer Curls $arm",
                                 "Lunges $leg", "Kettle Ball Swings $arm$leg", "Calf Raises $leg", "Farmer's Carry $arm$leg")
    var expanded by remember { mutableStateOf(false) }
    var reps by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    val maxNoteLength = 300

    // Collect workout sets from ViewModel
    val cardList by viewModel.workoutSets.collectAsState()

    // Dialog states
    var showWorkoutCompleteDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var setToDelete by remember { mutableStateOf<WorkoutSet?>(null) }

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
        Spacer(modifier = Modifier.height(32.dp))

        // Exercise Type Dropdown
        Box {
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

        Spacer(modifier = Modifier.height(16.dp))

        // Reps Input
        TextField(
            value = reps,
            onValueChange = { reps = it },
            label = { Text("Reps") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = reps.isNotBlank() && reps.toDoubleOrNull() == null
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Weight Input
        TextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = weight.isNotBlank() && weight.toDoubleOrNull() == null
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Notes Input
        Column(modifier = Modifier.fillMaxWidth()) {
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

        Spacer(modifier = Modifier.height(32.dp))

        // Add Set Button
        Button(
            onClick = {
                val exercise = WorkoutSet(
                    id = UUID.randomUUID().toString(),
                    name = exerciseType,
                    reps = reps,
                    weight = weight,
                    notes = notes
                )
                viewModel.addSet(exercise)
                reps = ""
                weight = ""
                notes = ""
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isValidInput
        ) {
            Text("Add Set")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Complete Workout Button
        if (cardList.isNotEmpty()) {
            Button(
                onClick = { showWorkoutCompleteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Complete Workout")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Sets List
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
                            Text("Exercise: ${set.name}")
                            Text("Reps: ${set.reps}")
                            Text("Weight: ${set.weight}")
                            if (set.notes.isNotBlank()) {
                                Text("Notes: ${set.notes}")
                            }
                        }

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

    // Workout Complete Dialog
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
                    Text("Total Sets: ${cardList.size}")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.saveWorkout()
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

    // Delete Confirmation Dialog
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
                        setToDelete?.id?.let { viewModel.removeSet(it) }
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

    BottomNavBar(navController)
}