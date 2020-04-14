package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.io.NIOClient
import kotlinmud.service.MobService

class RemoveMobOnClientDisconnectObserver(private val mobService: MobService) : Observer {
    override val eventType: EventType = EventType.CLIENT_DISCONNECTED

    override fun <T> processEvent(event: Event<T>) {
        val client = event.subject as NIOClient
        println("remove mob ${client.mob}")
        mobService.removeMob(client.mob!!)
    }
}
