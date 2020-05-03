package kotlinmud.service

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.event.DayEvent
import kotlinmud.event.event.PulseEvent
import kotlinmud.event.event.TickEvent

const val TICKS_IN_DAY = 20
const val TICK_LENGTH_IN_SECONDS = 20

class TimeService(private val eventService: EventService, private var time: Int = 0) {
    private var pulse = 0
    private var lastSecond = 0

    init {
        println("initialized with time $time")
    }

    fun loop() {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ss")).toInt()
        if (time != lastSecond) {
            lastSecond = time
            pulse()
        }
    }

    fun getTime(): Int {
        return time
    }

    private fun pulse() {
        pulse++
        eventService.publish(Event(EventType.PULSE, PulseEvent()))
        if (pulse * 2 > TICK_LENGTH_IN_SECONDS) {
            pulse = 0
            tick()
        }
    }

    private fun tick() {
        time++
        eventService.publish(Event(EventType.TICK, TickEvent()))
        val hour = time % TICKS_IN_DAY
        println("tick occurred. hour: $hour, time: $time")
        if (hour == 0) {
            eventService.publish(Event(EventType.DAY, DayEvent()))
            println("a new day has started")
        }
    }
}
