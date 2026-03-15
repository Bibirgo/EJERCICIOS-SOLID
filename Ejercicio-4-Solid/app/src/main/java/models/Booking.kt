package models

data class Booking(
    val room: Room,
    val guest: Guest,
    val nights: Int,
    val total: Double
)