package kotlinmud.service

import kotlinmud.event.Event
import kotlinmud.event.event.SendMessageToRoomEvent
import kotlinmud.event.observer.Observer

class EventService {
    var observers: List<Observer> = listOf()

    fun <T> publish(event: Event<T>) {
        observers.filter { it.eventType == event.eventType }
            .forEach { it.processEvent(event) }
    }

    fun publishRoomMessage(event: Event<SendMessageToRoomEvent>) {
        publish(event)
    }
}
