package kotlinmud.event.observer.impl.tick

import kotlinmud.mob.service.MobService

fun decrementAffectTimeoutEvent(mobService: MobService) {
    mobService.decrementAffects()
}
