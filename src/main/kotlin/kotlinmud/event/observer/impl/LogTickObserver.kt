package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.io.service.NIOServerService
import kotlinmud.mob.service.MobService
import org.slf4j.LoggerFactory

class LogTickObserver(private val mobService: MobService, private val serverService: NIOServerService) : Observer {
    private val logger = LoggerFactory.getLogger(LogTickObserver::class.java)
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        logger.info("tick :: {} clients, {} mobs", serverService.getClients().size, mobService.getMobRooms().size)
    }
}
