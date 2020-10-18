package kotlinmud.event.observer.impl.pulse

import kotlinmud.mob.service.MobService
import kotlinx.coroutines.runBlocking

fun pruneDeadMobsEvent(mobService: MobService) {
    runBlocking { mobService.pruneDeadMobs() }
}
