# MAD302-LAB03-YOURNAME

**Course:** MAD302 | **Lab:** 3 | **Student:** Your Full Name | **ID:** 123456789

## Description
An Android app that requests camera permission, simulates a network API call using
Kotlin coroutines (delay of 2 seconds), displays the result in a TextView, and
handles both permission denied and network failure error states gracefully.

## Features
- Camera permission request using ActivityResultContracts
- Async data fetch simulated with lifecycleScope.launch + delay(2000)
- Error handling with try-catch for simulated network failure
- UI feedback for loading, success, permission denied, and error states