package kotlinmud.generator

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.fs.loader.area.model.reset.MobReset
import kotlinmud.helper.math.coinFlip
import kotlinmud.mob.model.Mob
import kotlinmud.mob.type.Rarity
import kotlinmud.mob.type.Size
import kotlinmud.room.model.Room

class MobGeneratorService {
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

    fun generateMobs(biomes: List<Biome>): Map<BiomeType, List<Mob>> {
        var autoIncrementId = 1
        val mobBiomeMap = mutableMapOf<BiomeType, MutableList<Mob>>()
        biomes.forEach {
            mobBiomeMap[it.biomeType] = mutableListOf()
            it.mobs.forEach { mobBuilder ->
                val mob = mobBuilder
                    .id(autoIncrementId++)
                    .build()
                mobBiomeMap[it.biomeType]!!.add(mob)
            }
        }
        return mobBiomeMap
    }

    fun generateMobResets(rooms: List<Room>, mobs: Map<BiomeType, List<Mob>>): List<MobReset> {
        val resets = mutableListOf<MobReset>()
        val surfaceRooms = rooms.filter { it.biome.isSurface() }
        var resetId = 0
        surfaceRooms.filter {
            coinFlip()
        }.forEach {
            if (mobs[it.biome]!!.isEmpty()) {
                return@forEach
            }
            val mob = mobs[it.biome]!!.random()
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
}
