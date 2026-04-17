# MAD302-LAB03-Darshilkumar-Karkar

**Lab Title:** Lab 3 - Async Operations & Permissions  
**Name:** Darshilkumar Karkar  
**Student ID:** A00203357  

## Description
This Android application demonstrates best practices for handling asynchronous operations and runtime permissions. 

### Key Features:
* **Async Operations:** Simulates a network API call using Kotlin Coroutines (`lifecycleScope`) with a 2-second delay.
* **Permission Handling:** Requests Camera permission using the `ActivityResultContracts` API.
* **Error Handling:** Gracefully handles permission denial and simulates random network failures (40% chance) using try-catch blocks.
* **UI Feedback:** Displays real-time status updates (Loading, Success, or Error) in a TextView.
