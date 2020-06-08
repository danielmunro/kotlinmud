package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.Observer
import kotlinmud.event.type.EventType
import kotlinmud.io.service.ServerService
import kotlinmud.mob.service.MobService
import org.slf4j.LoggerFactory

class LogTickObserver(private val mobService: MobService, private val serverService: ServerService) : Observer {
    private val logger = LoggerFactory.getLogger(LogTickObserver::class.java)
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        logger.info("tick :: {} clients, {} mobs", serverService.getClients().size, mobService.getMobRooms().size)
    }
}
