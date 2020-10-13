package kotlinmud.generator.service

import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.generator.config.GeneratorConfig
import kotlinmud.generator.constant.DEPTH
import kotlinmud.generator.constant.DEPTH_GROUND
import kotlinmud.generator.constant.DEPTH_UNDERGROUND
import kotlinmud.generator.type.Layer
import kotlinmud.generator.type.Matrix3D
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

class CreateRoomService {
    val rooms = mutableListOf<RoomDAO>()
    fun generate(config: GeneratorConfig, elevationLayer: Layer, biomeLayer: Layer): Matrix3D {
        var index = 0
        return Array(DEPTH) { z ->
            transaction {
                Array(config.length) { y ->
                    IntArray(config.width) { x ->
                        addRoom(rooms, z, elevationLayer[y][x], BiomeType.fromIndex(biomeLayer[y][x]))
                        index++
                    }
                }
            }
        }
    }

    private fun addRoom(rooms: MutableList<RoomDAO>, z: Int, elevationValue: Int, biomeType: BiomeType) {
        rooms.add(
            buildRoom(
                z,
                elevationValue,
                biomeType
            )
        )
    }

    private fun buildRoom(z: Int, elevation: Int, biomeType: BiomeType): RoomDAO {
        val biome = when {
            z < DEPTH_UNDERGROUND -> BiomeType.UNDERGROUND
            z < DEPTH_GROUND + elevation -> biomeType
            else -> BiomeType.SKY
        }
        return RoomDAO.new {
            name = "todo"
            description = "todo"
            area = "todo"
            this.biome = biome
            substrate = if (biome == BiomeType.UNDERGROUND) { SubstrateType.ROCK } else { SubstrateType.NONE }
        }
    }
}
