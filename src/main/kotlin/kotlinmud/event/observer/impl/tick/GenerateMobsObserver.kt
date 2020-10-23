package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.generator.service.MobGeneratorService

class GenerateMobsObserver(private val mobGeneratorService: MobGeneratorService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        mobGeneratorService.respawnMobs()
    }
}
