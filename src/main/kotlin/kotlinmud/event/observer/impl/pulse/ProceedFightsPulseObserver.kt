package kotlinmud.event.observer.impl.pulse

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.helper.logger
import kotlinmud.mob.service.MobService

class ProceedFightsPulseObserver(private val mobService: MobService) : Observer {
    private val logger = logger(this)
    override val eventType: EventType = EventType.PULSE

    override fun <T> processEvent(event: Event<T>) {
        val rounds = mobService.proceedFights()
        if (rounds.isNotEmpty()) {
            logger.info("fights :: {}", rounds.size)
        }
    }
}
