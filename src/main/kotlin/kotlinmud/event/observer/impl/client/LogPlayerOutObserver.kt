package kotlinmud.event.observer.impl.client

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.io.model.Client
import kotlinmud.mob.service.MobService

class LogPlayerOutObserver(private val mobService: MobService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as Client) {
            this.mob?.let {
                it.loggedIn = false
                mobService.removeMob(it)
            }
        }
    }
}
