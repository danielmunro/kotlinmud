package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.service.EventService
import kotlinmud.helper.time.eventually
import kotlinmud.item.service.ItemService
import kotlinmud.mob.controller.MobController
import kotlinmud.mob.repository.findMobsByJobType
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinx.coroutines.runBlocking

class ScavengerCollectsItemsObserver(
    private val mobService: MobService,
    private val itemService: ItemService,
    private val eventService: EventService
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        findMobsByJobType(JobType.SCAVENGER).forEach {
            eventually {
                runBlocking {
                    MobController(mobService, itemService, eventService, it).pickUpAnyItem()
                }
            }
        }
    }
}
