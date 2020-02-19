package kotlinmud.event.observer

import kotlinmud.event.*

class TickObserver : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.TICK)
    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        println("tick")
        @Suppress("UNCHECKED_CAST")
        return EventResponse(Tick() as A)
    }
}
