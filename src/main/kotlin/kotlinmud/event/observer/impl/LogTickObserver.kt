package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.io.NIOServer
import kotlinmud.service.MobService

class LogTickObserver(private val mobService: MobService, private val server: NIOServer) : Observer {
    override val eventType: EventType = EventType.TICK
    override fun <T> processEvent(event: Event<T>) {
        println("${server.getClients().size} connected clients.\n${mobService.getMobRooms().size} mobs in realm.")
    }
}
