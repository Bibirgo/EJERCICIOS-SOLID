package models

data class Assistant(
    val name: String,
    val email: String,
    val registeredActivities: MutableList<Activity> = mutableListOf()
)