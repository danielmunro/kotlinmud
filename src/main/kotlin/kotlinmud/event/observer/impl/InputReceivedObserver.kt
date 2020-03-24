package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.event.InputReceivedEvent
import kotlinmud.event.observer.Observer
import kotlinmud.io.Request
import kotlinmud.service.MobService

class InputReceivedObserver(private val mobService: MobService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.INPUT_RECEIVED)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        val input = event.subject as InputReceivedEvent
        val client = input.client
        val room = mobService.getRoomForMob(client.mob)
        client.addRequest(Request(client.mob, input.input, room))

        @Suppress("UNCHECKED_CAST")
        return EventResponse(event as A)
    }
}
