package kotlinmud.event.observer.impl.pulse

import kotlinmud.mob.service.MobService

fun pruneDeadMobsEvent(mobService: MobService) {
    mobService.pruneDeadMobs()
}
