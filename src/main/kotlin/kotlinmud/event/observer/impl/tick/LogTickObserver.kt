package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.helper.logger
import kotlinmud.io.service.ServerService
import kotlinmud.mob.table.Mobs
import org.jetbrains.exposed.sql.selectAll

class LogTickObserver(private val serverService: ServerService) : Observer {
    private val logger = logger(this)
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        logger.info("tick :: {} clients, {} mobs", serverService.getClients().size, Mobs.selectAll().count())
    }
}
