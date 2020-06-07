package kotlinmud.world.generation

import kotlinmud.room.model.Exit
import kotlinmud.room.model.Room
import kotlinmud.room.model.RoomBuilder
import kotlinmud.room.type.Direction
import kotlinmud.world.BiomeType
import kotlinmud.world.biome.Biome

typealias Blocks = Array<Array<IntArray>>

typealias Layer = Array<IntArray>

const val DEPTH = 100
const val DEPTH_UNDERGROUND = 40
const val DEPTH_GROUND = 50

class GeneratorService(
    private val width: Int,
    private val length: Int,
    private val biomes: List<Biome>
) {
    private val biomeService = BiomeService(width, length, biomes)
    private var id = 0

    fun generate(): World {
        val rooms = mutableListOf<Room>()
        val biomeLayer = biomeService.createLayer((width * length) / (width * length / 10))
        val elevationService = ElevationService(biomeLayer, biomes)
        val elevationLayer = elevationService.buildLayer()
        var index = 0
        val world = World(rooms, Array(DEPTH) { z ->
            Array(length) { y ->
                IntArray(width) { x ->
                    rooms.add(
                        buildRoom(
                            z,
                            elevationLayer[y][x],
                            BiomeType.fromIndex(biomeLayer[y][x])
                        )
                    )
                    val thisIndex = index
                    index++
                    thisIndex
                }
            }
        })
        hookUpRoomExits(world)
        return world
    }

    private fun hookUpRoomExits(world: World) {
        for (z in world.blocks.indices) {
            for (y in world.blocks[z].indices) {
                for (x in world.blocks[z][y].indices) {
                    val id = world.blocks[z][y][x]
                    val room = world.rooms[id]
                    if (world.blocks[z][y].size > x + 1) {
                        val destId = world.blocks[z][y][x + 1]
                        val dest = world.rooms[destId]
                        room.exits.add(Exit(dest, Direction.EAST))
                        dest.exits.add(Exit(room, Direction.WEST))
                    }
                    if (world.blocks[z].size > y + 1) {
                        val destId = world.blocks[z][y + 1][x]
                        val dest = world.rooms[destId]
                        room.exits.add(Exit(dest, Direction.SOUTH))
                        dest.exits.add(Exit(room, Direction.NORTH))
                    }
                    if (z + 1 < DEPTH) {
                        val destId = world.blocks[z + 1][y][x]
                        val dest = world.rooms[destId]
                        room.exits.add(Exit(dest, Direction.DOWN))
                        dest.exits.add(Exit(room, Direction.UP))
                    }
                }
            }
        }
    }

    private fun buildRoom(z: Int, elevation: Int, biomeType: BiomeType): Room {
        val entityId = id
        id++
        val biome = when {
            z < DEPTH_UNDERGROUND -> BiomeType.UNDERGROUND
            z < DEPTH_GROUND + elevation -> biomeType
            else -> BiomeType.SKY
        }
        return RoomBuilder()
            .id(entityId)
            .name("todo")
            .description("todo")
            .biome(biome)
            .build()
    }
}
