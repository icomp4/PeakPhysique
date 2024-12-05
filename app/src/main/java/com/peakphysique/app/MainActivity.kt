package com.peakphysique.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.peakphysique.app.controller.Navigation
import com.peakphysique.app.ui.theme.PeakPhysiqueTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PeakPhysiqueTheme() {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().background(
                    Color(0xFFE0E0E0)
                )) {
                    Navigation()
                }
            }
        }
    }
}