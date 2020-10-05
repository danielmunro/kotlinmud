package kotlinmud.event.observer.impl.tick

import kotlinmud.generator.service.MobGeneratorService

fun generateMobsEvent(mobGeneratorService: MobGeneratorService) {
    mobGeneratorService.respawnMobs()
}
