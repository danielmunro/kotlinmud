package kotlinmud.event.observer.impl.client

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.PlayerLoggedInEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.mob.helper.MobBuilder
import kotlinmud.mob.service.MobService
import org.jetbrains.exposed.sql.transactions.transaction

class LogPlayerInObserver(private val mobService: MobService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as PlayerLoggedInEvent) {
            transaction {
                client.mob = mobService.findPlayerMobByName(mobCard.mobName)
                mobCard.loggedIn = true
            }
        }
    }
}
