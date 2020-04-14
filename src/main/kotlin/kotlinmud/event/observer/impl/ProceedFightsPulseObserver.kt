package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.service.MobService

class ProceedFightsPulseObserver(private val mobService: MobService) : Observer {
    override val eventType: EventType = EventType.PULSE

    override fun <T> processEvent(event: Event<T>) {
        val rounds = mobService.proceedFights()
        if (rounds.isNotEmpty()) {
            println("pulse has ${rounds.size} fight rounds")
        }
    }
}
