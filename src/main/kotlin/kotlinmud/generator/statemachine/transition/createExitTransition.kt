package kotlinmud.generator.statemachine.transition

import kotlinmud.generator.model.World
import kotlinmud.generator.service.CreateRoomService
import kotlinmud.generator.service.ExitCreationService
import kotlinmud.generator.service.WorldGeneration

fun createExitTransition(worldGeneration: WorldGeneration, createRoomService: CreateRoomService) {
    worldGeneration.world = World(
        createRoomService.rooms,
        worldGeneration.matrix!!
    )
    ExitCreationService(worldGeneration.world!!).hookUpRoomExits()
}
