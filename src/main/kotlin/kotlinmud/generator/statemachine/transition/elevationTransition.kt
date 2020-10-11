package kotlinmud.generator.statemachine.transition

import kotlinmud.biome.type.Biome
import kotlinmud.generator.service.ElevationService
import kotlinmud.generator.service.WorldGeneration

fun elevationTransition(worldGeneration: WorldGeneration, biomes: List<Biome>) {
    worldGeneration.elevationLayer =
        ElevationService(worldGeneration.biomeLayer!!, biomes).buildLayer()
}
