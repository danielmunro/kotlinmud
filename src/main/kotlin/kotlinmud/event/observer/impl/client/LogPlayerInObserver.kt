package kotlinmud.event.observer.impl.client

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.PlayerLoggedInEvent
import kotlinmud.event.observer.type.Observer
import org.jetbrains.exposed.sql.transactions.transaction

class LogPlayerInObserver : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as PlayerLoggedInEvent) {
            transaction {
                client.mob = mobCard.mob
                mobCard.loggedIn = true
            }
        }
    }
}
