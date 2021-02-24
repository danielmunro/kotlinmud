package kotlinmud.event.observer.impl.round

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.mob.fight.Round
import kotlinmud.mob.service.MobService
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction

class WimpyObserver(private val mobService: MobService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as Round) {
            transaction {
                getParticipants().forEach {
                    if (isActive() && it.wimpy > it.hp && it.room.getAllExits().isNotEmpty()) {
                        runBlocking { mobService.flee(it) }
                    }
                }
            }
        }
    }
}
