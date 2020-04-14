package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.service.MobService

class PruneDeadMobsPulseObserver(private val mobService: MobService) : Observer {
    override val eventType: EventType = EventType.PULSE

    override fun <T> processEvent(event: Event<T>) {
        mobService.pruneDeadMobs()
    }
}
