package logic

import models.Activity

class ValidatorSchedule {
    // Summary: Checks if two activities overlap in schedule.
    fun hasOverlap(newActivity: Activity, registeredActivities: List<Activity>): Boolean {
        return registeredActivities.any { existingActivity ->
            newActivity.startTime < existingActivity.endTime && newActivity.endTime > existingActivity.startTime
        }
    }

    fun hasAvailableSlots(activity: Activity): Boolean = activity.registeredAttendees.size < activity.maxCapacity
}