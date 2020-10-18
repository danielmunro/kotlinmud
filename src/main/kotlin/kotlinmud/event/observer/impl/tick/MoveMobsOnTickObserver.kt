package kotlinmud.event.observer.impl.tick

import kotlinmud.helper.time.eventually
import kotlinmud.mob.repository.findMobsWantingToMoveOnTick
import kotlinmud.mob.service.MobService
import kotlinx.coroutines.runBlocking

fun moveMobsOnTickEvent(mobService: MobService) {
    findMobsWantingToMoveOnTick().forEach {
        eventually {
            runBlocking {
                mobService.createMobController(it).move()
            }
        }
    }
}
