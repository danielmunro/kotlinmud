package kotlinmud.generator.statemachine.transition

import kotlinmud.generator.config.GeneratorConfig
import kotlinmud.generator.service.BiomeService
import kotlinmud.generator.service.WorldGeneration

fun biomeTransition(
    worldGeneration: WorldGeneration,
    biomeService: BiomeService,
    config: GeneratorConfig
) {
    worldGeneration.biomeLayer =
        biomeService.createLayer((config.width * config.length) / (config.width * config.length / 10))
}
