package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.helper.time.eventually
import kotlinmud.mob.repository.findMobsWantingToMoveOnTick
import kotlinmud.mob.service.MobService

class MoveMobsOnTickObserver(private val mobService: MobService) : Observer {
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        findMobsWantingToMoveOnTick().forEach {
            eventually {
                mobService.createMobController(it).move()
            }
        }
    }
}
