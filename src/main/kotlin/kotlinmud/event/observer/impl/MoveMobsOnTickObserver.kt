package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.mob.MobController
import kotlinmud.service.EventService
import kotlinmud.service.ItemService
import kotlinmud.service.MobService
import kotlinmud.time.eventually

class MoveMobsOnTickObserver(private val mobService: MobService, private val itemService: ItemService, private val eventService: EventService) :
    Observer {
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        mobService.getMobRooms().filter { it.mob.wantsToMove() }.forEach {
            eventually {
                MobController(mobService, itemService, eventService, it.mob).move()
            }
        }
    }
}
