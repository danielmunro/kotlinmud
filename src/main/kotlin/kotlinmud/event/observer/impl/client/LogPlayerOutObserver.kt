package kotlinmud.event.observer.impl.client

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.io.model.Client
import kotlinmud.player.repository.findMobCardByName

class LogPlayerOutObserver : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as Client) {
            findMobCardByName(this.mob?.name ?: return)!!.loggedIn = false
        }
    }
}
