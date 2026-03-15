package edu.itvo.sistemadereservasdehotel

import models.*
import logic.*
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class HotelTest {

    private lateinit var validator: ReservationValidator
    private lateinit var system: ReservationsSystem

    @Before
    fun setUp() {
        // Single initialization: Dependency Inversion Principle (DIP)
        validator = ReservationValidator()
        system = ReservationsSystem(validator)
    }

    @Test
    fun `check availability and calculate cost per night`() {
        // Suite Room: $600.0 per night
        val room = Room(101, "Suite", 600.0)
        val guest = Guest("Bibiana Rubi Gaytan Ortiz", "22920147")

        // Booking for 4 nights
        val success = system.createBooking(guest, room, 4)

        assertTrue("The booking should be successful", success)

        // Calculation: 600.0 * 4 = 2400.0
        val actualTotal = guest.bookingHistory.last().total
        assertEquals(2400.0, actualTotal, 0.0)
        assertFalse("The room should be occupied", room.available)

        println("Booking 1 - Guest: ${guest.name} | Total: $$actualTotal | Room Status: Occupied")
    }

    @Test
    fun `should not allow booking an occupied room`() {
        val room = Room(200, "Single", 400.0)
        val guest1 = Guest("Bibiana Rubi", "9514415705")
        val guest2 = Guest("Ambrosio Cardoso Jimenez", "DNI002")

        // Guest books first
        system.createBooking(guest1, room, 2)

        // Guest 2 tries to book the same room
        val result = system.createBooking(guest2, room, 1)

        assertFalse("The system correctly blocked the double booking", result)
        println("Booking 2 - Double booking attempt successfully blocked")
    }

    @Test
    fun `cancel booking and release room`() {
        val room = Room(300, "Double", 500.0)
        val guest = Guest("Eneyda Juliet", "9510000000")

        system.createBooking(guest, room, 3)
        val bookingToCancel = guest.bookingHistory.last()

        // Cancellation process
        system.cancelBooking(guest, bookingToCancel)

        assertTrue("The room became available for other guests", room.available)
        assertEquals(0, guest.bookingHistory.size)
        println("Booking 3 - Cancellation successful | Room ${room.number} now available: ${room.available}")
    }
}