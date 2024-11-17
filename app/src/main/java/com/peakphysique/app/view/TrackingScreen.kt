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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.peakphysique.app.controller.BottomNavBar
import java.util.UUID

/*

    TODO: Add a start/end workout button
    TODO: Add the workout to database
    TODO: Fix bug allowing user to submit an exercise as "Select Exercise"
    TODO: Fix bug allowing user to submit blank exercises

 */
@Composable
fun TrackingScreen(navController: NavHostController) {
    // State for exercise dropdown
    var exerciseType by remember { mutableStateOf("Select Exercise") }
    val exerciseOptions = listOf("Squat", "Bench Press", "Deadlift") // Example options
    var expanded by remember { mutableStateOf(false) }
    var cardList by remember { mutableStateOf(listOf<Set>()) }

    // State for input fields
    var reps by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    val maxNoteLength = 300

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
        Spacer(modifier = Modifier.height(32.dp))

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

        TextField(
            value = reps,
            onValueChange = { reps = it },
            label = { Text("Reps") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = reps.isNotBlank() && reps.toDoubleOrNull() == null
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = weight.isNotBlank() && weight.toDoubleOrNull() == null
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

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

    BottomNavBar(navController)
}

data class Set(
    val id: String,
    val name: String,
    val reps: String,
    val weight: String,
    val notes: String
)