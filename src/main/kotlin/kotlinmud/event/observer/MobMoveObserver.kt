package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.event.MobMoveEvent
import kotlinmud.service.MobService

class MobMoveObserver(private val mobService: MobService) : Observer {
    override val eventTypes: Array<EventType> = arrayOf(EventType.MOB_MOVE)

    override fun <T> processEvent(event: Event<T>) {
        if (event.subject is MobMoveEvent) {
            mobService.moveMob(event.subject.mob, event.subject.room)
        }
    }
}
