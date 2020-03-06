package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.mob.JobType
import kotlinmud.random.dN
import kotlinmud.service.MobService

class MoveScavengersOnTickObserver(private val mobService: MobService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.TICK)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        mobService.getMobRooms().filter {
            it.mob.job == JobType.SCAVENGER
        }.forEach {
            val exit = it.room.exits.random()
            mobService.moveMob(it.mob, exit.destination, exit.direction)
        }
        @Suppress("UNCHECKED_CAST")
        return EventResponse(event as A)
    }
}
