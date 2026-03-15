package interfaces

import models.Booking
import models.Guest
import models.Room

interface IReservationSystem {


        fun createBooking(guest: Guest, room: Room, nights: Int): Boolean

        fun cancelBooking(guest: Guest, booking: Booking)
}