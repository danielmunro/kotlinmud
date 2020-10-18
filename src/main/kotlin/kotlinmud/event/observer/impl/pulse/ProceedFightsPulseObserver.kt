package kotlinmud.event.observer.impl.pulse

import kotlinmud.mob.service.MobService
import kotlinx.coroutines.runBlocking

fun proceedFightsEvent(mobService: MobService) {
    runBlocking { mobService.proceedFights() }
}
