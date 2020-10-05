package kotlinmud.event.observer.impl.tick

import kotlinmud.helper.logger
import kotlinmud.io.service.ServerService
import kotlinmud.mob.table.Mobs
import org.jetbrains.exposed.sql.selectAll
import org.slf4j.Logger

fun logTickObserver(serverService: ServerService, logger: Logger) {
    logger.info("tick :: {} clients, {} mobs", serverService.getClients().size, Mobs.selectAll().count())
}
