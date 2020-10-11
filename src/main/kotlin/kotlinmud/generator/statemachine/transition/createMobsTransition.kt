package kotlinmud.generator.statemachine.transition

import kotlinmud.biome.type.Biome
import kotlinmud.generator.service.MobGeneratorService

fun createMobsTransition(biomes: List<Biome>) {
    MobGeneratorService(biomes).respawnMobs()
}
