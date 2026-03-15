package interfaces

import models.Activity
import models.Assistant

interface IEventsSystem {


        fun registerAttendee(attendee: Assistant, activity: Activity): Boolean

}

