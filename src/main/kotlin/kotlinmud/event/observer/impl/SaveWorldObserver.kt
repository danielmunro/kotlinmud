package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.fs.saver.WorldSaver

class SaveWorldObserver(private val worldSaver: WorldSaver) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.DAY)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        worldSaver.save()
        @Suppress("UNCHECKED_CAST")
        return EventResponse(event as A)
    }
}
