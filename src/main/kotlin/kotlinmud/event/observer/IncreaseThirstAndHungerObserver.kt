package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.Tick
import kotlinmud.event.WrongEventTypeException
import kotlinmud.io.Server
import kotlinmud.service.MobService

class IncreaseThirstAndHungerObserver(private val mobService: MobService, private val server: Server) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.TICK)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        if (event.subject !is Tick) {
            throw WrongEventTypeException()
        }

        val mobs = mobService.getMobRooms().filter { !it.mob.isNpc }.map { it.mob }
        val clients = server.getClientsFromMobs(mobs)

        clients.forEach {
            it.mob.appetite.decrement()
            if (it.mob.appetite.isHungry()) {
                it.write("You are hungry.")
            }
            if (it.mob.appetite.isThirsty()) {
                it.write("You are thirsty.")
            }
        }

        @Suppress("UNCHECKED_CAST")
        return EventResponse(event.subject as A)
    }
}
