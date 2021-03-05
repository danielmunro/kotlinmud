package kotlinmud.generator.statemachine.transition

import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.ResourceType
import kotlinmud.helper.math.dice
import kotlinmud.room.service.RoomService

fun createJungleTransition(roomService: RoomService) {
    roomService.findByBiome(BiomeType.JUNGLE).forEach { room ->
        repeat(dice(1, 3) - 1) {
            room.resources.add(ResourceType.JUNGLE_TREE)
        }
    }
}
