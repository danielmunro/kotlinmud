package kotlinmud.generator.service

import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.generator.config.GeneratorConfig
import kotlinmud.generator.constant.DEPTH
import kotlinmud.generator.constant.DEPTH_GROUND
import kotlinmud.generator.constant.DEPTH_UNDERGROUND
import kotlinmud.generator.type.Layer
import kotlinmud.generator.type.Matrix3D
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area

class CreateRoomService(private val roomService: RoomService) {
    val rooms = mutableListOf<Room>()
    fun generate(config: GeneratorConfig, elevationLayer: Layer, biomeLayer: Layer): Matrix3D {
        var index = 0
        return Array(DEPTH) { z ->
            Array(config.length) { y ->
                IntArray(config.width) { x ->
                    addRoom(rooms, z, elevationLayer[y][x], BiomeType.fromIndex(biomeLayer[y][x]))
                    index++
                }
            }
        }
    }

    private fun addRoom(rooms: MutableList<Room>, z: Int, elevationValue: Int, biomeType: BiomeType) {
        rooms.add(
            buildRoom(
                z,
                elevationValue,
                biomeType
            )
        )
    }

    private fun buildRoom(z: Int, elevation: Int, biomeType: BiomeType): Room {
        val biome = when {
            z < DEPTH_UNDERGROUND -> BiomeType.UNDERGROUND
            z < DEPTH_GROUND + elevation -> biomeType
            else -> BiomeType.SKY
        }
        return RoomBuilder(roomService)
            .name("todo")
            .description("todo")
            .area(Area.None)
            .substrate(if (biome == BiomeType.UNDERGROUND) { SubstrateType.ROCK } else { SubstrateType.NONE })
            .build()
    }
}
