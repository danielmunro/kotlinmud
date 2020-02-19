package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.Pulse
import kotlinmud.service.MobService

class PulseObserver(private val mobService: MobService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.PULSE)
    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        val rounds = mobService.proceedFights()
        println("pulse has ${rounds.size} rounds")
        @Suppress("UNCHECKED_CAST")
        return EventResponse(Pulse() as A)
    }
}
