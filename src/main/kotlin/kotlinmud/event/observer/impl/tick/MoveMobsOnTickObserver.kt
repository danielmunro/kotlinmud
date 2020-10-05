package kotlinmud.event.observer.impl.tick

import kotlinmud.helper.time.eventually
import kotlinmud.mob.repository.findMobsWantingToMoveOnTick
import kotlinmud.mob.service.MobService

fun moveMobsOnTickEvent(mobService: MobService) {
    findMobsWantingToMoveOnTick().forEach {
        eventually {
            mobService.createMobController(it).move()
        }
    }
}
