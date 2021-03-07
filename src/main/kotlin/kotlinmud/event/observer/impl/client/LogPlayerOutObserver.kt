package kotlinmud.event.observer.impl.client

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.io.model.Client

class LogPlayerOutObserver : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as Client) {
            this.mob?.loggedIn = false
        }
    }
}
