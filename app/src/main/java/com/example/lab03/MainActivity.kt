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
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.lab03.R
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

/**
 * Main activity for the Lab 3 Android application.
 * Handles UI setup, permission requests, and async data fetching.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var btnFetch: Button
    private lateinit var tvResult: TextView

    /**
     * Called when the activity is first created.
     * Sets up UI references and click listeners.
     *
     * @param savedInstanceState Bundle containing saved state (if any)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bind views from layout
        btnFetch = findViewById(R.id.btnFetch)
        tvResult = findViewById(R.id.tvResult)

        // Trigger permission check on button click
        btnFetch.setOnClickListener {
            checkPermissionAndFetch()
        }
    }
    // Registers a permission launcher for CAMERA at activity creation time
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted — proceed to fetch data
                fetchData()
            } else {
                // Permission denied — inform the user clearly
                tvResult.text = "Permission denied. Camera access is required."
            }
        }

    /**
     * Checks if camera permission is already granted.
     * If yes, fetches data directly. If no, requests permission.
     */

    private fun checkPermissionAndFetch() {
        when {
            // Check if permission is already granted
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                fetchData()
            }
            else -> {
                // Launch the permission request dialog
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }



    /**
     * Simulates an async API call using Kotlin coroutines.
     * Uses lifecycleScope to tie the coroutine to the Activity lifecycle.
     * Shows a loading message, waits 2 seconds, then displays the result.
     */
    private fun fetchData() {
        // Launch coroutine on the lifecycle-aware scope
        lifecycleScope.launch {
            // Show loading state immediately
            tvResult.text = "Loading..."

            try {
                delay(2000)
                val result = simulateApiCall()
                tvResult.text = "Success: $result"

            } catch (e: SecurityException) {
                // Handles unexpected permission revocation mid-session
                tvResult.text = "Permission error: ${e.message}"

            } catch (e: Exception) {
                // Handles simulated network failure or any other error
                tvResult.text = "Network error: ${e.message}"
            }
        }
    }
    /**
     * Simulates an API call that may randomly fail to mimic network errors.
     * Throws an exception 40% of the time to test error handling.
     *
     * @throws Exception when simulated network failure occurs
     * @return Mock API response string on success
     */
    private suspend fun simulateApiCall(): String {
        delay(2000) // Simulate network latency

        // Simulate a network failure ~40% of the time
        if (Math.random() < 0.4) {
            throw Exception("Network timeout: server did not respond")
        }

        return "User: Jane Doe | Score: 98 | Status: Active"
    }
}