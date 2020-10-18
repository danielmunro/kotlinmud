package kotlinmud.event.observer.impl.tick

import kotlinmud.mob.service.MobService
import kotlinx.coroutines.runBlocking

fun regenMobsEvent(mobService: MobService) {
    runBlocking { mobService.regenMobs() }
}
