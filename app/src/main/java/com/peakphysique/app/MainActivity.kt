package com.peakphysique.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.peakphysique.app.controller.Navigation
import com.peakphysique.app.ui.theme.PeakPhysiqueTheme
import com.peakphysique.app.view.RegisterScreen

/**
 * Main entry point of the Peak Physique application.
 * Responsible for setting up the app's UI container and theme.
 *
 * Features:
 * - Edge-to-edge display support
 * - Custom theme implementation
 * - Navigation setup
 */
class MainActivity : ComponentActivity() {
    /**
     * Initializes the activity and sets up the main UI components.
     *
     * The setup includes:
     * - Enabling edge-to-edge display
     * - Applying the app's theme
     * - Setting up the root container with background
     * - Initializing navigation
     *
     * @param savedInstanceState Bundle containing the activity's previously saved state
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display for better visual experience
        enableEdgeToEdge()

        // Set up the main content with Jetpack Compose
        setContent {
            PeakPhysiqueTheme() {
                // Root container with centered content and light gray background
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE0E0E0))  // Light gray background
                ) {
                    // Initialize main navigation component
                    Navigation()
                }
            }
        }
    }
}