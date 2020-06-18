package kotlinmud.generator

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.fs.loader.area.model.reset.MobReset
import kotlinmud.helper.math.coinFlip
import kotlinmud.mob.model.Mob
import kotlinmud.mob.type.Rarity
import kotlinmud.mob.type.Size
import kotlinmud.room.model.Room

class MobGeneratorService(private val biomes: List<Biome>) {
    private val biomeMobList = mutableMapOf<BiomeType, List<Mob>>()

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

    fun init() {
        var autoIncrementId = 1
        biomes.forEach {
            biomeMobList[it.biomeType] = it.mobs.map { mobBuilder ->
                mobBuilder
                    .id(autoIncrementId++)
                    .build()
            }
        }
    }

    fun generateMobResets(rooms: List<Room>): List<MobReset> {
        val resets = mutableListOf<MobReset>()
        var resetId = 0
        getRoomsToPopulate(rooms).forEach { room ->
            getRandomBiomeMob(room.biome)?.let { mob ->
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

    fun getAllMobs(): List<Mob> {
        return biomeMobList.flatMap { it.value }
    }

    private fun biomeHasMobs(biomeType: BiomeType): Boolean {
        return biomeMobList[biomeType]?.isNotEmpty() ?: false
    }

    private fun getRandomBiomeMob(biomeType: BiomeType): Mob? {
        return biomeMobList[biomeType]?.random()
    }

    private fun getRoomsToPopulate(rooms: List<Room>): List<Room> {
        return rooms.filter { it.biome.isSurface() && biomeHasMobs(it.biome) }
            .filter {
                coinFlip()
            }
    }
}
