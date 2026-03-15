package edu.itvo.gestiondeeventos

import models.*
import logic.*
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class EventsTest {

    private lateinit var validator: ValidatorSchedule
    private lateinit var eventSystem: SystemEvents

    @Before
    fun setUp() {
        // Inicialización única para mantener el orden y limpieza
        validator  = ValidatorSchedule()
        eventSystem = SystemEvents(validator )
    }

    @Test
    fun shouldNotAllowAttendeeToRegisterForOverlappingActivities() {
        val speaker  = Speaker("Ambrosio Cardoso Jiménez", "Programacion")
        val workshop1  = Activity("Taller Arquitectura de Software Limpia", speaker , 800, 1100, 11)
        val workshop2  = Activity("Inteligencia de Negocios", speaker , 1000, 1200, 11)
        val attendee  = Assistant("Bibiana Rubi", "bibianagaytan2602@gmail.com")

        // Intento 1
        val result1 = eventSystem.registerAttendee(attendee, workshop1)
        println("Attendee: ${attendee.name} | Activity: ${workshop1.name} | Speaker: ${workshop1.speaker.name} | Time: ${workshop1.startTime} | Result: ${if(result1) "SUCCESSFUL" else "FAILED"}")

        // Second attempt (Overlapping)
        val result2 = eventSystem.registerAttendee(attendee, workshop2)
        println("Attendee: ${attendee.name} | Activity: ${workshop2.name} | Speaker: ${workshop2.speaker.name} | Time: ${workshop2.startTime} | Result: ${if(result2) "SUCCESSFUL" else "FAILED"}")

        assertFalse(result2)
    }

    @Test
    fun shouldValidateMaximumCapacityPerActivity() {
        val speaker = Speaker("Ambrosio Cardoso Jiménez", "Programming")
        val talk = Activity("Platica Aplicación de SOLID", speaker, 1500, 1600, 2)

        val attendees = listOf(
            Assistant("User 1", "u1@test.com"),
            Assistant("User 2", "u2@test.com"),
            Assistant("User 3", "u3@test.com")
        )

        attendees.forEach { attendee ->
            val result = eventSystem.registerAttendee(attendee, talk)
            println("Attendee: ${attendee.name} | Activity: ${talk.name} | Speaker: ${speaker.name} | Time: ${talk.startTime} | Result: ${if(result) "SUCCESSFUL" else "FAILED (FULL CAPACITY)"}")
        }
    }

    @Test
    fun shouldDisplaySortedScheduleByAttendee() {
        val attendee = Assistant("Bibiana Rubi", "bibianagaytan2602@gmail.com")
        val speaker = Speaker("P1", "D")
        val afternoon = Activity("Cierre", speaker, 1500, 1700, 20)
        val morning = Activity("Apertura", speaker, 900, 1200, 20)

        eventSystem.registerAttendee(attendee, afternoon)
        eventSystem.registerAttendee(attendee, morning)

        val schedule = eventSystem.getAttendeeSchedule(attendee)

        println("\n--- SCHEDULE REPORT ---")
        schedule.forEach { activity ->
            println("Attendee: ${attendee.name} | Speaker: ${activity.speaker.name} | Time: ${activity.startTime} hrs | Activity: ${activity.name}")
        }
        println("------------------------")

        assertEquals("Apertura", schedule[0].name)
    }
}
