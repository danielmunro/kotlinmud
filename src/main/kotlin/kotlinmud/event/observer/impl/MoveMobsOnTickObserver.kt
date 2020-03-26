package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.mob.MobController
import kotlinmud.service.EventService
import kotlinmud.service.MobService
import kotlinmud.time.eventually

class MoveMobsOnTickObserver(private val mobService: MobService, private val eventService: EventService) :
    Observer {
    override val eventTypes: List<EventType> = listOf(EventType.TICK)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        mobService.getMobRooms().filter { it.mob.wantsToMove() }.forEach {
            eventually {
                MobController(mobService, eventService, it.mob).move()
            }
        }
        @Suppress("UNCHECKED_CAST")
        return EventResponse(event as A)
    }
}
