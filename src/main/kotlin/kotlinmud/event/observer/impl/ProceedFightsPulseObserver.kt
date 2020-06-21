package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.mob.service.MobService
import org.slf4j.LoggerFactory

class ProceedFightsPulseObserver(private val mobService: MobService) :
    Observer {
    private val logger = LoggerFactory.getLogger(ProceedFightsPulseObserver::class.java)
    override val eventType: EventType = EventType.PULSE

    override fun <T> processEvent(event: Event<T>) {
        val rounds = mobService.proceedFights()
        if (rounds.isNotEmpty()) {
            logger.info("fights :: {}", rounds.size)
        }
    }
}
