package kotlinmud.event.observer

import kotlinmud.MobService
import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.MobMoveEvent
import org.jetbrains.exposed.sql.transactions.transaction

class MobMoveObserver(private val mobService: MobService) : Observer {
    override fun getEventTypes(): Array<EventType> {
        return arrayOf(EventType.MOB_MOVE)
    }

    override fun processEvent(event: Event) {
        if (event is MobMoveEvent) {
            transaction {
                event.room.exits.find { it.direction == event.direction }
                    .also { mobService.moveMob(event.mob, it!!.destination) }
            }
        }
    }
}