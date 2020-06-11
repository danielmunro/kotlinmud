package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.Observer
import kotlinmud.event.type.EventType
import kotlinmud.fs.service.PersistenceService

class SaveVersionsObserver(private val persistenceService: PersistenceService) : Observer {
    override val eventType: EventType = EventType.DAY

    override fun <T> processEvent(event: Event<T>) {
        persistenceService.writeVersionFile()
    }
}
