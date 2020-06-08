package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.io.model.Client
import kotlinmud.mob.service.MobService
import org.slf4j.LoggerFactory

class RemoveMobOnClientDisconnectObserver(private val mobService: MobService) : Observer {
    private val logger = LoggerFactory.getLogger(RemoveMobOnClientDisconnectObserver::class.java)
    override val eventType: EventType = EventType.CLIENT_DISCONNECTED

    override fun <T> processEvent(event: Event<T>) {
        val client = event.subject as Client
        logger.debug("client disconnected, remove mob :: {}", client.mob)
        mobService.removeMob(client.mob!!)
    }
}
