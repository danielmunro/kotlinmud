package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.service.EventService
import kotlinmud.helper.time.eventually
import kotlinmud.mob.controller.MobController
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinx.coroutines.runBlocking

class ScavengerCollectsItemsObserver(
    private val mobService: MobService,
    private val eventService: EventService
) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        mobService.findMobsByJobType(JobType.SCAVENGER).forEach {
            eventually {
                runBlocking {
                    MobController(mobService, eventService, it).pickUpAnyItem()
                }
            }
        }
    }
}
