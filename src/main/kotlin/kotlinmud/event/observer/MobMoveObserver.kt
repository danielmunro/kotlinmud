package kotlinmud.event.observer

import kotlinmud.MobService
import kotlinmud.event.Event
import kotlinmud.event.EventType
import org.jetbrains.exposed.sql.transactions.transaction

class MobMoveObserver(private val mobService: MobService) : Observer {
    override val eventTypes: Array<EventType> = arrayOf(EventType.MOB_MOVE)

    override fun processEvent(event: Event) {
        mobService.moveMob(event.mob, event.room)
    }
}