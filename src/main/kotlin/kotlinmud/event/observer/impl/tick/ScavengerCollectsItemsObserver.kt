package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
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

class ScavengerCollectsItemsObserver(
    private val mobService: MobService,
    private val itemService: ItemService,
    private val eventService: EventService
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        MobDAO.wrapRows(
            Mobs.select { Mobs.job eq JobType.SCAVENGER.toString() }
        ).forEach {
            eventually {
                runBlocking {
                    MobController(mobService, itemService, eventService, it).pickUpAnyItem()
                }
            }
        }
    }
}
