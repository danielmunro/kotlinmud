package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.generator.MobGeneratorService

class GenerateMobsObserver(private val mobGeneratorService: MobGeneratorService) : Observer {
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        mobGeneratorService.respawnMobs()
    }
}
