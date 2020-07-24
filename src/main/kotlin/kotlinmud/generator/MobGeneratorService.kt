package kotlinmud.generator

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.type.Rarity
import kotlinmud.mob.type.Size

class MobGeneratorService(biomes: List<Biome>) {
    private val biomeMobList: Map<BiomeType, Biome> = biomes.map {
        Pair(it.biomeType, it)
    }.toMap()

    init {
        biomes.forEach {
            biomeMobList.plus(Pair(it.biomeType, it))
        }
    }

    companion object {
        private fun getMaxInRoom(mob: MobDAO): Int {
            return when (mob.race.size) {
                Size.TINY -> 3
                Size.SMALL -> 3
                Size.MEDIUM -> 2
                Size.LARGE -> 2
                Size.HUGE -> 1
            }
        }

        private fun getMaxInRoomModifier(mob: MobDAO): Int {
            return when (mob.rarity) {
                Rarity.COMMON -> 1
                Rarity.UNCOMMON -> 1
                Rarity.RARE -> 1
            }
        }

        private fun getMaxInWorld(mob: MobDAO): Int {
            return when (mob.rarity) {
                Rarity.COMMON -> 500
                Rarity.UNCOMMON -> 100
                Rarity.RARE -> 10
            }
        }
    }

    fun getAllMobs(): List<MobDAO> {
        return listOf()
//        return biomeMobList.flatMap { it.value }
    }
}
