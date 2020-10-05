package kotlinmud.event.observer.impl.tick

import kotlinmud.mob.service.MobService

fun regenMobsEvent(mobService: MobService) {
    mobService.regenMobs()
}
