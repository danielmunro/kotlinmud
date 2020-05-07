package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.service.MobService
import org.slf4j.LoggerFactory

class ProceedFightsPulseObserver(private val mobService: MobService) : Observer {
    private val logger = LoggerFactory.getLogger(ProceedFightsPulseObserver::class.java)
    override val eventType: EventType = EventType.PULSE

    override fun <T> processEvent(event: Event<T>) {
        val rounds = mobService.proceedFights()
        if (rounds.isNotEmpty()) {
            logger.info("fights :: {}", rounds.size)
        }
    }
}
