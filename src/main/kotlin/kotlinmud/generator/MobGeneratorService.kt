package kotlinmud.generator

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeMobList
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

        private fun biomeHasMobs(biomeType: BiomeType, mobs: BiomeMobList): Boolean {
            return mobs[biomeType]?.isNotEmpty() ?: false
        }

        private fun getRandomBiomeMob(biomeType: BiomeType, mobs: BiomeMobList): Mob? {
            return mobs[biomeType]?.random()
        }

        private fun getRoomsToPopulate(rooms: List<Room>, mobs: BiomeMobList): List<Room> {
            return rooms.filter { it.biome.isSurface() && biomeHasMobs(it.biome, mobs) }
                .filter {
                    coinFlip()
                }
        }
    }

    fun generateMobs(biomes: List<Biome>): BiomeMobList {
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

    fun generateMobResets(rooms: List<Room>, mobs: BiomeMobList): List<MobReset> {
        val resets = mutableListOf<MobReset>()
        var resetId = 0
        getRoomsToPopulate(rooms, mobs).forEach { room ->
            getRandomBiomeMob(room.biome, mobs)?.let { mob ->
                resetId++
                resets.add(
                    MobReset(
                        resetId,
                        mob.id,
                        room.id,
                        (getMaxInRoom(mob) - getMaxInRoomModifier(mob)).coerceAtLeast(1),
                        getMaxInWorld(mob)
                    )
                )
            }
        }
        return resets
    }
}
