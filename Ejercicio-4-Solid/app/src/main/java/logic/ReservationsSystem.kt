package logic

import interfaces.IReservationSystem
import models.Booking
import models.Guest
import models.Room

class ReservationsSystem(private val validator: ReservationValidator) : IReservationSystem {

    override fun createBooking(guest: Guest, room: Room, nights: Int): Boolean {
        if (validator.isAvailable(room)) {
            val total = room.pricePerNight * nights
            val newBooking = Booking(room, guest, nights, total)

            room.available = false
            guest.bookingHistory.add(newBooking)

            println("SUCCESS: Booking created for ${guest.name}. Total: $$total")
            return true
        }
        println("ERROR: Room ${room.number} is not available.")
        return false
    }

    override fun cancelBooking(guest: Guest, booking: Booking) {
        booking.room.available = true
        guest.bookingHistory.remove(booking)
        println("CANCELLATION: Room ${booking.room.number} released.")
    }
}