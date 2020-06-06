package kotlinmud.world.generation

import java.util.*
import kotlin.random.Random.Default.nextInt
import kotlinmud.room.model.Exit
import kotlinmud.room.model.Room
import kotlinmud.room.model.RoomBuilder
import kotlinmud.room.type.Direction
import kotlinmud.world.BiomeType

typealias Blocks = Array<Array<IntArray>>

typealias Layer = Array<IntArray>

const val DEPTH = 100
const val DEPTH_UNDERGROUND = 40
const val DEPTH_SKY = 60

class Generator(private val width: Int, private val length: Int) {
    val biomeLayer = Array(length) {
        IntArray(width) {
            0
        }
    }

    private var id = 0

    companion object {
        fun randomBiome(): Int {
            val biomeType = BiomeType.getAll().random()
            return BiomeType.values().indexOf(biomeType)
        }
    }

    fun generate(): World {
        val rooms = mutableListOf<Room>()
        val biomeLayer = createBiomeLayer((width * length) / (width * length / 10))
        var index = 0
        val world = World(rooms, Array(DEPTH) { z ->
            Array(length) { y ->
                IntArray(width) { x ->
                    rooms.add(buildRoom(z, BiomeType.fromIndex(biomeLayer[y][x])))
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

    private fun buildRoom(z: Int, biomeType: BiomeType): Room {
        val entityId = id
        id++
        return RoomBuilder()
            .id(entityId)
            .name("todo")
            .description("todo")
            .biome(when {
                z < DEPTH_UNDERGROUND -> BiomeType.UNDERGROUND
                z < DEPTH_SKY -> biomeType
                else -> BiomeType.SKY
            })
            .build()
    }

    private fun createBiomeLayer(biomeCount: Int): Layer {
        val coordinates = mutableListOf<Pair<Int, Int>>()
        for (i in 1..biomeCount) {
            val w = nextInt(0, width)
            val l = nextInt(0, length)
            val biome = randomBiome()
            biomeLayer[l][w] = biome
            coordinates.add(Pair(l, w))
        }
        var iteration = 0
        while (coordinates.size > 0) {
            coordinates.removeIf {
                !drawAround(
                    Pair(it.first - iteration, it.second - iteration),
                    Pair(it.first + iteration, it.second + iteration),
                    biomeLayer[it.first][it.second]
                )
            }
            iteration++
        }
        return biomeLayer
    }

    private fun drawAround(c1: Pair<Int, Int>, c2: Pair<Int, Int>, value: Int): Boolean {
        var drawn = false
        for (l in c1.first - 1..c2.first) {
            for (w in c1.second - 1..c2.second) {
                if (isInBounds(l, w) && ((w <= c1.first || w >= c2.first) || (l <= c1.second || l >= c2.second)) && biomeLayer[l][w] == 0) {
                    biomeLayer[l][w] = value
                    drawn = true
                }
            }
        }
        return drawn
    }

    private fun isInBounds(x: Int, y: Int): Boolean {
        return x in 0 until length && y in 0 until width
    }
}
