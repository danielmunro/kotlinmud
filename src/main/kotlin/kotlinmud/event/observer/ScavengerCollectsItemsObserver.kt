package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.mob.JobType
import kotlinmud.mob.Mob
import kotlinmud.mob.MobController
import kotlinmud.service.EventService
import kotlinmud.service.MobService
import kotlinmud.time.eventually

class ScavengerCollectsItemsObserver(
    private val mobService: MobService,
    private val eventService: EventService
) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.TICK)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        mobService.getMobRooms().filter {
            it.mob.job == JobType.SCAVENGER
        }.forEach {
            eventually { pickUpItem(it.mob) }
        }
        @Suppress("UNCHECKED_CAST")
        return EventResponse(event as A)
    }

    private fun pickUpItem(mob: Mob) {
        MobController(mobService, eventService, mob).pickUpAnyItem()
    }
}
