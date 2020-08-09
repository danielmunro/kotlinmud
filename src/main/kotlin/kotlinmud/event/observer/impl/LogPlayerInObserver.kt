package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.PlayerLoggedInEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType

class LogPlayerInObserver : Observer {
    override val eventType: EventType = EventType.CLIENT_LOGGED_IN

    override fun <T> processEvent(event: Event<T>) {
        with(event.subject as PlayerLoggedInEvent) {
            client.mob = mobCard.mob
            mobCard.loggedIn = true
        }
    }
}
