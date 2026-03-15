package models

data class Activity(
    val name: String,
    val speaker: Speaker,
    val startTime: Int, // Using 24-hour format (e.g., 1400 for 2:00 PM)
    val endTime: Int,
    val maxCapacity: Int,
    val registeredAttendees: MutableList<Assistant> = mutableListOf()
)
