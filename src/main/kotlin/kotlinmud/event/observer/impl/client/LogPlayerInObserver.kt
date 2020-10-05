package kotlinmud.event.observer.impl.client

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.PlayerLoggedInEvent

fun logPlayerInEvent(event: Event<*>) {
    with(event.subject as PlayerLoggedInEvent) {
        client.mob = mobCard.mob
        mobCard.loggedIn = true
    }
}
