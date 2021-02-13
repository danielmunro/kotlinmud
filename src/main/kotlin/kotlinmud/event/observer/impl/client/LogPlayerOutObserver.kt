package kotlinmud.event.observer.impl.client

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.io.model.Client
import kotlinmud.player.repository.findMobCardByName
import org.jetbrains.exposed.sql.transactions.transaction

class LogPlayerOutObserver : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as Client) {
            transaction {
                findMobCardByName(this@with.mob?.name ?: return@transaction)!!.loggedIn = false
            }
        }
    }
}
