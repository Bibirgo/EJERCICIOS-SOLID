package models

data class Guest(
    val name: String,
    val dni: String,
    val bookingHistory: MutableList<Booking> = mutableListOf()
)