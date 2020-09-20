package kotlinmud.event.observer.impl.client

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.io.model.Client
import kotlinmud.player.repository.findMobCardByName

class LogPlayerOutObserver : Observer {
    override val eventType: EventType = EventType.CLIENT_DISCONNECTED

    override fun <T> processEvent(event: Event<T>) {
        with(event.subject as Client) {
            findMobCardByName(this.mob?.name ?: return)?.let {
                it.loggedIn = false
            }
        }
    }
}
