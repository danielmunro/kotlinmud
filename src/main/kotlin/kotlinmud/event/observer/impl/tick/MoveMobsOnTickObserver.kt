package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.mob.service.MobService

class MoveMobsOnTickObserver(private val mobService: MobService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
//        mobService.findMobsWantingToMoveOnTick().forEach {
//            eventually {
//                mobService.createMobController(it).move()
//            }
//        }
    }
}
