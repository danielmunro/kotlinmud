package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.helper.time.eventually
import kotlinmud.mob.repository.findMobsWantingToMoveOnTick
import kotlinmud.mob.service.MobService

class MoveMobsOnTickObserver(private val mobService: MobService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        findMobsWantingToMoveOnTick().forEach {
            eventually {
                mobService.createMobController(it).move()
            }
        }
    }
}
