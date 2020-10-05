package kotlinmud.event.observer.impl.client

import kotlinmud.event.impl.Event
import kotlinmud.io.model.Client
import kotlinmud.player.repository.findMobCardByName

fun logPlayerOutEvent(event: Event<*>) {
    with(event.subject as Client) {
        findMobCardByName(this.mob?.name ?: return)!!.loggedIn = false
    }
}
