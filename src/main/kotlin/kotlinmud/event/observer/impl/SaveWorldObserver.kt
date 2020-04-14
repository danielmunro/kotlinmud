package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.fs.saver.WorldSaver

class SaveWorldObserver(private val worldSaver: WorldSaver) : Observer {
    override val eventType: EventType = EventType.DAY

    override fun <T> processEvent(event: Event<T>) {
        worldSaver.save()
    }
}
