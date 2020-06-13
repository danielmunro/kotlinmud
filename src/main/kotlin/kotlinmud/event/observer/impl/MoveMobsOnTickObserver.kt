package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.Observer
import kotlinmud.event.service.EventService
import kotlinmud.event.type.EventType
import kotlinmud.helper.time.eventually
import kotlinmud.item.service.ItemService
import kotlinmud.mob.controller.MobController
import kotlinmud.mob.service.MobService

class MoveMobsOnTickObserver(private val mobService: MobService, private val itemService: ItemService, private val eventService: EventService) :
    Observer {
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        mobService.getMobRooms().filter { it.mob.wantsToMove() }.forEach {
            eventually {
                MobController(mobService, itemService, eventService, it.mob)
                    .move()
            }
        }
    }
}
