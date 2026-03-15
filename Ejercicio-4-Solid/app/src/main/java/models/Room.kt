package models

data class Room(
    val number: Int,
    val type: String,
    val pricePerNight: Double,
    var available: Boolean = true
)