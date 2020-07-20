package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.PlayerLoggedInEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.mob.service.MobService
import org.jetbrains.exposed.sql.transactions.transaction

class LogPlayerInObserver(private val mobService: MobService) :
    Observer {
    override val eventType: EventType = EventType.CLIENT_LOGGED_IN

    override fun <T> processEvent(event: Event<T>) {
        val playerLoggedInEvent = event.subject as PlayerLoggedInEvent
        val mob = transaction {
            playerLoggedInEvent.mobCard.mob!!
        }
        playerLoggedInEvent.client.mob = mob
        mobService.addMob(mob)
    }
}
