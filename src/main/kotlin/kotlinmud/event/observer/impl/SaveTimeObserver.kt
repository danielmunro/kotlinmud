package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.fs.saver.saveTime
import kotlinmud.service.TimeService

class SaveTimeObserver(private val timeService: TimeService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.DAY)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        saveTime(timeService)

        @Suppress("UNCHECKED_CAST")
        return EventResponse(event as A)
    }
}
