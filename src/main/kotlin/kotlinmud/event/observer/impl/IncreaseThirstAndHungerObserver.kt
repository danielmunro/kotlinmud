package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.io.NIOServer
import kotlinmud.service.MobService

class IncreaseThirstAndHungerObserver(private val mobService: MobService, private val server: NIOServer) :
    Observer {
    override val eventTypes: List<EventType> = listOf(EventType.TICK)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        val mobs = mobService.getMobRooms().filter { !it.mob.isNpc }.map { it.mob }
        val clients = server.getClientsFromMobs(mobs)
        clients.forEach {
            with(it.mob!!) {
                appetite.decrement()
                if (appetite.isHungry()) {
                    it.writePrompt("You are hungry.")
                }
                if (appetite.isThirsty()) {
                    it.writePrompt("You are thirsty.")
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        return EventResponse(event.subject as A)
    }
}
