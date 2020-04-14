package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.fs.saver.saveTime
import kotlinmud.service.TimeService

class SaveTimeObserver(private val timeService: TimeService) : Observer {
    override val eventType: EventType = EventType.DAY

    override fun <T> processEvent(event: Event<T>) {
        saveTime(timeService)
    }
}
