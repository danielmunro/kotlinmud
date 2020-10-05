package kotlinmud.event.observer.impl.pulse

import kotlinmud.mob.service.MobService

fun proceedFightsEvent(mobService: MobService) {
    mobService.proceedFights()
}
