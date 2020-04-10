package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.io.NIOClient
import kotlinmud.service.MobService

class RemoveMobOnClientDisconnectObserver(private val mobService: MobService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.CLIENT_DISCONNECTED)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        val client = event.subject as NIOClient
        println("remove mob ${client.mob}")
        mobService.removeMob(client.mob!!)

        @Suppress("UNCHECKED_CAST")
        return EventResponse(event.subject as A)
    }
}
