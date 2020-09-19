package kotlinmud.event.observer.impl.pulse

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.mob.service.MobService

class ProceedFightsPulseObserver(private val mobService: MobService) : Observer {
    override val eventType: EventType = EventType.PULSE

    override fun <T> processEvent(event: Event<T>) {
        mobService.proceedFights()
    }
}
