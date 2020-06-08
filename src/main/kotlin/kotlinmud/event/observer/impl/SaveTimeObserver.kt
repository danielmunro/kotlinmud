package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.Observer
import kotlinmud.event.type.EventType
import kotlinmud.service.PersistenceService
import kotlinmud.service.TimeService

class SaveTimeObserver(private val timeService: TimeService, private val persistenceService: PersistenceService) : Observer {
    override val eventType: EventType = EventType.DAY

    override fun <T> processEvent(event: Event<T>) {
        persistenceService.writeTimeFile(timeService)
    }
}
