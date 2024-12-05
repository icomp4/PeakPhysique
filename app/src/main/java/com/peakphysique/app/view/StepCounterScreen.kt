package com.peakphysique.app.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.peakphysique.app.controller.BottomNavBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun StepCounterScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val currentDate = remember { LocalDate.now().toString() }

    var selectGoal by remember { mutableStateOf("Select New Step Goal") }
    val stepGoalOptions = listOf("1000", "3000", "5000", "7000", "10000", "20000")
    var expanded by remember { mutableStateOf(false) }
    var progress by remember { mutableIntStateOf(0) }

    var initialStepCount by remember { mutableIntStateOf(0) }

    val stepHistory = remember { mutableMapOf<String, Int>() }
    var stepCount by remember { mutableIntStateOf(0) }
    var targetSteps by remember { mutableIntStateOf(loadTargetSteps(context)) }


    // Initialize sensor manager
    val sensorManager = context.getSystemService(SensorManager::class.java)
    val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    //add dummy data for step history
    LaunchedEffect(Unit) {
        initializeStepHistory(stepHistory)
    }

    // Recalculate progress every time stepCount or targetSteps change
    LaunchedEffect(targetSteps, stepCount) {
        progress = (stepCount.toFloat() / targetSteps.toFloat()).coerceAtMost(1f).toInt()
    }

    //Check for sensor
    if (stepSensor == null) {
        Log.e("StepCounterScreen", "Step counter sensor not available on this device.")
    }

    //Check if permission already granted or request it
    if (!hasActivityRecognitionPermission(context)) {
        requestActivityRecognitionPermission(context as Activity) // Pass your activity context
    }

    //Establish Listener to track steps daily
    val stepSensorListener = rememberUpdatedState(
        object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {

                    // Check if the day has changed
                    val thisDate = LocalDate.now().toString()

                    // If the date has changed, reset step count for the new day
                    if (thisDate != currentDate) {
                        initialStepCount = event.values[0].toInt()
                        stepCount = 0
                    }

                    // Calculate the steps taken since the app started, considering daily reset
                    stepCount = (event.values[0].toInt() - initialStepCount)

                    // Calculate progress towards the target steps goal
                    progress =
                        (stepCount.toFloat() / targetSteps.toFloat()).coerceAtMost(1f).toInt()
                }
            }
        }
    )

    // register the step counter sensor( to receive updates)
    LaunchedEffect(stepSensor) {
        stepSensor?.let {
            sensorManager.registerListener(
                stepSensorListener.value,
                it,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    // Unregister the sensor listener when the Composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            sensorManager?.unregisterListener(stepSensorListener.value)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.85f)
            .padding(45.dp)
            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Steps Counted",
            fontSize = 32.sp
        )
        Text(
            modifier = Modifier.padding(15.dp),
            text = "$stepCount",
            fontSize = 48.sp
        )

        CircularProgressIndicator(
            progress = {
                (stepCount.toFloat() / targetSteps.toFloat()).coerceAtMost(1f)
            },
            modifier = Modifier
                .padding(10.dp)
                .size(130.dp),
            color = if (stepCount >= targetSteps) Color.Green else MaterialTheme.colorScheme.primary,
            strokeWidth = 8.dp,
        )
        // If goal is met toast congrats message
        if (stepCount >= targetSteps) {
            Toast.makeText(LocalContext.current, "\uD83C\uDF89 You Reached Your Goal. Great Job! \uD83C\uDF89", Toast.LENGTH_SHORT).show()
        }

        Text(
            modifier = Modifier
                .padding(15.dp),
            text = "Current Goal: $targetSteps Steps",
            fontSize = 18.sp
        )

        Box {
            // Display and handle dropdown menu for selecting goal
            Text(
                text = selectGoal,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))

            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                stepGoalOptions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            selectGoal = option
                            expanded = false
                            targetSteps = option.toInt()
                            saveTargetSteps(context, targetSteps)
                        },
                        text = { Text(option) }
                    )
                }
            }
        }

        Text(
            modifier = Modifier.padding(20.dp),
            text = "Step History (Last 5 Days):",
            style = MaterialTheme.typography.titleMedium
        )
        stepHistory.forEach { (date, steps) ->
            Text(text = "$date : $steps steps")
        }
    }
    BottomNavBar(navController)
}

//create dummy data for stepHistory
fun initializeStepHistory(stepHistory: MutableMap<String, Int>) {
    val currentDate = LocalDate.now()
    val formatDate = DateTimeFormatter.ISO_DATE
    for (i in 0 until 5) {
        val date = currentDate.minusDays(i.toLong()) // Go backwards in days
        val formattedDate = date.format(formatDate) // Format date as YYYY-MM-DD
        val currentStepCount =
            Random.nextInt(1000, 10000) // Generate fake step count (between 1000 and 10000)
        stepHistory[formattedDate] = currentStepCount
    }
}

// Check if permission is already granted
@RequiresApi(Build.VERSION_CODES.Q)
fun hasActivityRecognitionPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACTIVITY_RECOGNITION
    ) == PackageManager.PERMISSION_GRANTED
}

// Request permission
@RequiresApi(Build.VERSION_CODES.Q)
fun requestActivityRecognitionPermission(activity: Activity) {
    if (ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) != PackageManager.PERMISSION_GRANTED
    ) {

        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
            100
        )
    }
}

// Persist Step Goal save and load
fun loadTargetSteps(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("target_steps", 7000) // Default to 7000 if no value exists
}
fun saveTargetSteps(context: Context, targetSteps: Int) {
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putInt("target_steps", targetSteps)
        apply()
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview
@Composable
fun TrackScreenPreview() {
    StepCounterScreen(rememberNavController())
}
