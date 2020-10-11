package kotlinmud.generator.statemachine.transition

import kotlinmud.generator.config.GeneratorConfig
import kotlinmud.generator.service.CreateRoomService
import kotlinmud.generator.service.WorldGeneration

fun createRoomsTransition(worldGeneration: WorldGeneration, createRoomService: CreateRoomService, config: GeneratorConfig) {
    worldGeneration.matrix =
        createRoomService.generate(config, worldGeneration.elevationLayer!!, worldGeneration.biomeLayer!!)
}
