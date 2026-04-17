/**
 * Course:      MAD302 - Mobile Application Development
 * Lab:         Lab 3 - Async Operations & Permissions
 * Name:        Darshilkumar Karkar
 * Student ID:  A00203357
 * Date:        21/04/2026
 * Description: Android app that requests camera permission, simulates
 *              an async API call using Kotlin coroutines, displays the
 *              result in a TextView, and handles errors gracefully.
 */


package com.example.lab03

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.lab03.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * MainActivity is the single screen of this application.
 * It coordinates permission requests, async data fetching,
 * and UI state updates based on the result of both operations.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var btnFetch: Button
    private lateinit var tvResult: TextView

    /**
     * Permission launcher registered at startup (before onCreate completes).
     * Uses the ActivityResultContracts API to request CAMERA permission.
     * Calls fetchData() on grant, shows denied message on refusal.
     */
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                fetchData()
            } else {
                // Inform user why the feature is unavailable
                tvResult.text = "Permission denied. Camera access is required to fetch data."
            }
        }

    /**
     * Called when the activity is first created.
     * Inflates the layout and sets up the button click listener.
     *
     * @param savedInstanceState Previously saved state bundle (may be null)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bind UI components from the inflated layout
        btnFetch = findViewById(R.id.btnFetch)
        tvResult = findViewById(R.id.tvResult)

        // Begin permission check when user taps the Fetch Data button
        btnFetch.setOnClickListener {
            checkPermissionAndFetch()
        }
    }

    /**
     * Checks current CAMERA permission status.
     * Proceeds to fetch data if already granted, otherwise requests it.
     */
    private fun checkPermissionAndFetch() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted — go straight to fetch
                fetchData()
            }
            else -> {
                // Trigger the system permission dialog
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    /**
     * Launches an async coroutine to simulate a network data fetch.
     * Uses lifecycleScope so the coroutine is automatically cancelled
     * if the Activity is destroyed before it completes.
     * Handles both success and failure states with appropriate UI updates.
     */
    private fun fetchData() {
        lifecycleScope.launch {
            // Show loading feedback immediately so user knows work is happening
            tvResult.text = "Loading..."

            try {
                // Call suspend function that simulates network latency
                val result = simulateApiCall()

                // Update UI on the main thread (guaranteed by lifecycleScope)
                tvResult.text = "Success:\n$result"

            } catch (e: SecurityException) {
                // Rare case: permission revoked while the app was running
                tvResult.text = "Permission error: ${e.message}"

            } catch (e: Exception) {
                // Catches the simulated network timeout or any other failure
                tvResult.text = "Network error: ${e.message}"
            }
        }
    }

    /**
     * Simulates an async API call with a 2-second delay.
     * Randomly throws an exception 40% of the time to test error handling.
     *
     * @throws Exception when simulated network failure occurs
     * @return A mock API response string representing fetched user data
     */
    private suspend fun simulateApiCall(): String {
        // Simulate network round-trip time
        delay(2000)

        // Randomly simulate a network failure (40% chance)
        if (Math.random() < 0.4) {
            throw Exception("Network timeout: server did not respond")
        }

        // Return mock data on success
        return "User: Jane Doe | Score: 98 | Status: Active"
    }
}