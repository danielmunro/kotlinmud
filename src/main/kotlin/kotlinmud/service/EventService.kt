package kotlinmud.service

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.event.SendMessageToRoomEvent
import kotlinmud.event.observer.Observer

class EventService {
    var observers: List<Observer> = listOf()

    fun <T, A> publish(event: Event<T>): EventResponse<A> {
        return observers.filter { it.eventTypes.contains(event.eventType) }
            .map { it.processEvent<T, A>(event) }
            .last()
    }

    fun publishRoomMessage(event: Event<SendMessageToRoomEvent>) {
        publish<SendMessageToRoomEvent, EventResponse<SendMessageToRoomEvent>>(event)
    }
}
