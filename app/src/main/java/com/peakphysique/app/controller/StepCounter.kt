package com.peakphysique.app.controller

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.activity.ComponentActivity


class StepCounter(context: Context) : ComponentActivity() {
    var hasSensor = false

    private val sensorManager: SensorManager by lazy {
        getSystemService(SENSOR_SERVICE) as SensorManager
    }

    private val sensor: Sensor? by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    fun checkForSensor() {
        if (sensor == null) {
            hasSensor = false
        } else
            hasSensor = true
    }
}