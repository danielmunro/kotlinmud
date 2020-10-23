package kotlinmud.event.observer.impl.pulse

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.mob.service.MobService

class PruneDeadMobsPulseObserver(private val mobService: MobService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        mobService.pruneDeadMobs()
    }
}
