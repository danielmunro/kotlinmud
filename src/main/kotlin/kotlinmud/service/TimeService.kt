package kotlinmud.service

import kotlinmud.event.Day
import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.Pulse
import kotlinmud.event.Tick
import kotlinmud.event.event.DayEvent
import kotlinmud.event.event.PulseEvent
import kotlinmud.event.event.TickEvent
import kotlinmud.io.TICK_LENGTH_IN_SECONDS

const val TICKS_IN_DAY = 2

class TimeService(private val eventService: EventService) {
    var pulse = 0
    var time = 0

    fun pulse() {
        pulse++
        eventService.publish<PulseEvent, Pulse>(Event(EventType.PULSE, PulseEvent()))
        if (pulse * 2 > TICK_LENGTH_IN_SECONDS) {
            eventService.publish<TickEvent, Tick>(Event(EventType.TICK, TickEvent()))
            pulse = 0
            time++
            val dayElapsed = time % TICKS_IN_DAY == 0
            println("tick occurred, hour ${time % TICKS_IN_DAY}${if (dayElapsed) ", day elapsed" else "" }")
            if (dayElapsed) {
                eventService.publish<DayEvent, Day>(Event(EventType.DAY, DayEvent()))
            }
        }
    }
}
