package kotlinmud.event.factory

import kotlinmud.event.impl.DayEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.PulseEvent
import kotlinmud.event.impl.TickEvent
import kotlinmud.event.type.EventType

fun createPulseEvent(): Event<PulseEvent> {
    return Event(EventType.PULSE, PulseEvent())
}

fun createTickEvent(): Event<TickEvent> {
    return Event(EventType.TICK, TickEvent())
}

fun createDayEvent(): Event<DayEvent> {
    return Event(EventType.DAY, DayEvent())
}
