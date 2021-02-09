package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.helper.logger
import kotlinmud.io.service.ServerService
import kotlinmud.mob.table.Mobs
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class LogTickObserver(private val serverService: ServerService) : Observer {
    private val logger = logger(this)

    override suspend fun <T> invokeAsync(event: Event<T>) {
        logger.info("tick :: {} clients, {} mobs", serverService.getClients().size, transaction { Mobs.selectAll().count() })
    }
}
