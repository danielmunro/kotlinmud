package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.Observer
import kotlinmud.event.type.EventType
import kotlinmud.mob.service.MobService

class RegenMobsObserver(private val mobService: MobService) : Observer {
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        mobService.regenMobs()
    }
}
