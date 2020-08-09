package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.io.model.Client

class LogPlayerOutObserver : Observer {
    override val eventType: EventType = EventType.CLIENT_DISCONNECTED

    override fun <T> processEvent(event: Event<T>) {
        val client = event.subject as Client
        client.mob?.mobCard?.loggedIn = false
    }
}
