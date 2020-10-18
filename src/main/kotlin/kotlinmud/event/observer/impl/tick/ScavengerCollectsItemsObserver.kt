package kotlinmud.event.observer.impl.tick

import kotlinmud.event.service.EventService
import kotlinmud.helper.time.eventually
import kotlinmud.item.service.ItemService
import kotlinmud.mob.controller.MobController
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.service.MobService
import kotlinmud.mob.table.Mobs
import kotlinmud.mob.type.JobType
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.select

fun scavengerCollectsItemEvent(mobService: MobService, itemService: ItemService, eventService: EventService) {
    MobDAO.wrapRows(
        Mobs.select { Mobs.job eq JobType.SCAVENGER.value }
    ).forEach {
        eventually {
            runBlocking {
                MobController(mobService, itemService, eventService, it).pickUpAnyItem()
            }
        }
    }
}
