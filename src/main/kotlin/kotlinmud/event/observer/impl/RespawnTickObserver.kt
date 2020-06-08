package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.Observer
import kotlinmud.event.type.EventType
import kotlinmud.service.RespawnService

class RespawnTickObserver(private val respawnService: RespawnService) : Observer {
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        respawnService.respawn()
    }
}
