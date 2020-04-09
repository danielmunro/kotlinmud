package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.Tick
import kotlinmud.event.observer.Observer
import kotlinmud.io.Server
import kotlinmud.service.MobService

class LogTickObserver(private val mobService: MobService, private val server: Server) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.TICK)
    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        println("${server.getClients().size} connected clients.\n${mobService.getMobRooms().size} mobs in realm.")
        @Suppress("UNCHECKED_CAST")
        return EventResponse(Tick() as A)
    }
}
