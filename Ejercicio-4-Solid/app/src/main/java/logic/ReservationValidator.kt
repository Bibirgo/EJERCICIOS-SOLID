package logic

import models.Room

class ReservationValidator {

    fun isAvailable(room: Room): Boolean = room.available
}