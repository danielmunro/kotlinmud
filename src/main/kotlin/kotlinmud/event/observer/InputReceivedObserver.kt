package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.event.InputReceivedEvent
import kotlinmud.event.response.InputReceivedResponse
import kotlinmud.service.MobService
import java.lang.Exception

class InputReceivedObserver(private val mobService: MobService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.INPUT_RECEIVED)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        if (event.subject is InputReceivedEvent) {
            val room = mobService.getRoomForMob(event.subject.client.mob)
            @Suppress("UNCHECKED_CAST")
            return InputReceivedResponse(room as A, room)
        }
        throw Exception()
    }
}