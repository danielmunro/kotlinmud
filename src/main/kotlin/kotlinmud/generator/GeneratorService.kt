package kotlinmud.generator

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.generator.constant.DEPTH
import kotlinmud.generator.constant.DEPTH_GROUND
import kotlinmud.generator.constant.DEPTH_UNDERGROUND
import kotlinmud.generator.model.World
import kotlinmud.generator.type.Layer
import kotlinmud.generator.type.Matrix3D
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

class GeneratorService(
    private val width: Int,
    private val length: Int,
    private val biomes: List<Biome>
) {
    private val biomeService = BiomeService(width, length, biomes)

    fun generate(): World {
        val rooms = mutableListOf<RoomDAO>()
        val biomeLayer = biomeService.createLayer((width * length) / (width * length / 10))
        val elevationLayer = ElevationService(biomeLayer, biomes).buildLayer()
        val mobGeneratorService = MobGeneratorService(biomes)
        val matrix = transaction { buildMatrix(rooms, elevationLayer, biomeLayer) }
        val mobResets = mobGeneratorService.generateMobResets(rooms)
        val world = World(
            rooms,
            matrix,
            mobGeneratorService.getAllMobs(),
            mobResets
        )
        transaction { hookUpRoomExits(world) }
        return world
    }

    private fun buildMatrix(rooms: MutableList<RoomDAO>, elevationLayer: Layer, biomeLayer: Layer): Matrix3D {
        var index = 0
        return Array(DEPTH) { z ->
            Array(length) { y ->
                IntArray(width) { x ->
                    addRoom(rooms, z, elevationLayer[y][x], BiomeType.fromIndex(biomeLayer[y][x]))
                    index++
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

    private fun hookUpRoomExits(world: World) {
        for (z in world.matrix3D.indices) {
            for (y in world.matrix3D[z].indices) {
                for (x in world.matrix3D[z][y].indices) {
                    hookUpRoom(world, x, y, z)
                }
            }
        }
    }

    private fun hookUpRoom(world: World, x: Int, y: Int, z: Int) {
        val id = world.matrix3D[z][y][x]
        val room = world.rooms[id]
        if (world.matrix3D[z][y].size > x + 1) {
            val destId = world.matrix3D[z][y][x + 1]
            val dest = world.rooms[destId]
            room.east = dest
            dest.west = room
        }
        if (world.matrix3D[z].size > y + 1) {
            val destId = world.matrix3D[z][y + 1][x]
            val dest = world.rooms[destId]
            room.south = dest
            dest.north = room
        }
        if (z + 1 < DEPTH) {
            val destId = world.matrix3D[z + 1][y][x]
            val dest = world.rooms[destId]
            room.up = dest
            dest.down = room
        }
    }

    private fun buildRoom(z: Int, elevation: Int, biomeType: BiomeType): RoomDAO {
        return RoomDAO.new {
            name = "todo"
            description = "todo"
            area = "todo"
            biome = when {
                z < DEPTH_UNDERGROUND -> BiomeType.UNDERGROUND
                z < DEPTH_GROUND + elevation -> biomeType
                else -> BiomeType.SKY
            }
        }
    }
}
