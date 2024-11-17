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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
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

    // State for delete confirmation
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var setToDelete by remember { mutableStateOf<Set?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Dropdown for Exercise Type
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

        // TextField for Reps
        TextField(
            value = reps,
            onValueChange = { reps = it },
            label = { Text("Reps") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TextField for Weight
        TextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TextField for Notes
        TextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Button to Add Set
        Button(
            onClick = {
                val exercise = Set(
                    id = UUID.randomUUID().toString(), // Generate unique ID
                    name = exerciseType,
                    reps = reps,
                    weight = weight,
                    notes = notes
                )
                cardList = cardList + exercise
                // Clear input fields after adding
                reps = ""
                weight = ""
                notes = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Set")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display cards in a scrollable list
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
                        // Set details
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Exercise: ${set.name}")
                            Text("Reps: ${set.reps}")
                            Text("Weight: ${set.weight}")
                            Text("Notes: ${set.notes}")
                        }

                        // Delete button
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
    val id: String, // for identifying each set
    val name: String,
    val reps: String,
    val weight: String,
    val notes: String
)