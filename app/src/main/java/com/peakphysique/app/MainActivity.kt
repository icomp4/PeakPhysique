package com.peakphysique.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.peakphysique.app.ui.theme.PeakPhysiqueTheme
import com.peakphysique.app.view.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PeakPhysiqueTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // Not sure if this is the best way to handle showing the register screen on start
                    LoginScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

