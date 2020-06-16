package kotlinmud.generator

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.fs.loader.area.model.reset.MobReset
import kotlinmud.generator.constant.DEPTH
import kotlinmud.generator.constant.DEPTH_GROUND
import kotlinmud.generator.constant.DEPTH_UNDERGROUND
import kotlinmud.generator.model.World
import kotlinmud.generator.type.Layer
import kotlinmud.generator.type.Matrix3D
import kotlinmud.helper.math.coinFlip
import kotlinmud.mob.model.Mob
import kotlinmud.mob.type.Rarity
import kotlinmud.mob.type.Size
import kotlinmud.room.model.Exit
import kotlinmud.room.model.Room
import kotlinmud.room.model.RoomBuilder
import kotlinmud.room.type.Direction

class GeneratorService(
    private val width: Int,
    private val length: Int,
    private val biomes: List<Biome>
) {
    companion object {
        private fun getMaxInRoom(mob: Mob): Int {
            return when (mob.race.size) {
                Size.TINY -> 3
                Size.SMALL -> 3
                Size.MEDIUM -> 2
                Size.LARGE -> 2
                Size.HUGE -> 1
            }
        }

        private fun getMaxInRoomModifier(mob: Mob): Int {
            return when (mob.rarity) {
                Rarity.COMMON -> 0
                Rarity.UNCOMMON -> 2
                Rarity.RARE -> 3
            }
        }

        private fun getMaxInWorld(mob: Mob): Int {
            return when (mob.rarity) {
                Rarity.COMMON -> 2500
                Rarity.UNCOMMON -> 500
                Rarity.RARE -> 50
            }
        }
    }

    private val biomeService = BiomeService(width, length, biomes)
    private var id = 0

    fun generate(): World {
        val rooms = mutableListOf<Room>()
        val biomeLayer = biomeService.createLayer((width * length) / (width * length / 10))
        val elevationLayer = ElevationService(biomeLayer, biomes).buildLayer()
        val world = World(
            rooms,
            buildMatrix(rooms, elevationLayer, biomeLayer),
            generateMobResets(rooms)
        )
        hookUpRoomExits(world)
        return world
    }

    private fun generateMobResets(rooms: List<Room>): List<MobReset> {
        val mobService = MobService()
        val resets = mutableListOf<MobReset>()
        val surfaceRooms = rooms.filter { it.biome.isSurface() }
        val biomeMap = mutableMapOf<BiomeType, Biome>()
        biomes.forEach {
            biomeMap[it.biomeType] = it
        }
        val mobBiomeMap = mutableMapOf<BiomeType, List<Mob>>()
        biomes.forEach {
            mobBiomeMap[it.biomeType] = it.mobs.map { mobBuilder ->
                mobService.add(mobBuilder).build()
            }
        }
        var resetId = 0
        surfaceRooms.filter {
            coinFlip()
        }.forEach {
            if (mobBiomeMap[it.biome]!!.isEmpty()) {
                return@forEach
            }
            val mob = mobBiomeMap[it.biome]!!.random()
            resetId++
            resets.add(
                MobReset(
                    resetId,
                    mob.id,
                    it.id,
                    (getMaxInRoom(mob) - getMaxInRoomModifier(mob)).coerceAtLeast(1),
                    getMaxInWorld(mob)
                )
            )
        }
        return resets
    }

    private fun buildMatrix(rooms: MutableList<Room>, elevationLayer: Layer, biomeLayer: Layer): Matrix3D {
        var index = 0
        return Array(DEPTH) { z ->
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
        }
    }

    private fun hookUpRoomExits(world: World) {
        for (z in world.matrix3D.indices) {
            for (y in world.matrix3D[z].indices) {
                for (x in world.matrix3D[z][y].indices) {
                    val id = world.matrix3D[z][y][x]
                    val room = world.rooms[id]
                    if (world.matrix3D[z][y].size > x + 1) {
                        val destId = world.matrix3D[z][y][x + 1]
                        val dest = world.rooms[destId]
                        room.exits.add(Exit(dest, Direction.EAST))
                        dest.exits.add(Exit(room, Direction.WEST))
                    }
                    if (world.matrix3D[z].size > y + 1) {
                        val destId = world.matrix3D[z][y + 1][x]
                        val dest = world.rooms[destId]
                        room.exits.add(Exit(dest, Direction.SOUTH))
                        dest.exits.add(Exit(room, Direction.NORTH))
                    }
                    if (z + 1 < DEPTH) {
                        val destId = world.matrix3D[z + 1][y][x]
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
