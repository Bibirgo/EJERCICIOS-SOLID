package logic

import interfaces.IEventsSystem
import models.Activity
import models.Assistant



class SystemEvents(private val validator: ValidatorSchedule) : IEventsSystem {

    override fun registerAttendee(attendee: Assistant, activity: Activity): Boolean {
        // 1. Validate available slots
        if (!validator.hasAvailableSlots(activity)) {
            println("ERROR: No available slots for activity ${activity.name}")
            return false
        }

        // 2. Validate schedule overlap
        if (validator.hasOverlap(activity, attendee.registeredActivities)) {
            println("ERROR: Attendee ${attendee.name} already has another activity at that time.")
            return false
        }

        // 3. Register attendance
        activity.registeredAttendees.add(attendee)
        attendee.registeredActivities.add(activity)
        println("SUCCESS: ${attendee.name} registered for ${activity.name}")
        return true
    }

    fun getAttendeesByActivity(activity: Activity): List<Assistant> {
        return activity.registeredAttendees
    }

    fun getAttendeeSchedule(attendee: Assistant): List<Activity> {
        return attendee.registeredActivities.sortedBy { it.startTime }
    }
}