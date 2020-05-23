package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventService
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.item.ItemService
import kotlinmud.mob.MobController
import kotlinmud.mob.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.time.eventually

class ScavengerCollectsItemsObserver(
    private val mobService: MobService,
    private val itemService: ItemService,
    private val eventService: EventService
) : Observer {
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        mobService.getMobRooms().filter {
            it.mob.job == JobType.SCAVENGER
        }.forEach {
            eventually {
                MobController(mobService, itemService, eventService, it.mob).pickUpAnyItem()
            }
        }
    }
}
