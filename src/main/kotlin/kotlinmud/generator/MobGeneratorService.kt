package kotlinmud.generator

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.fs.loader.area.model.reset.MobReset
import kotlinmud.helper.math.coinFlip
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.type.Rarity
import kotlinmud.mob.type.Size
import kotlinmud.room.dao.RoomDAO

class MobGeneratorService(biomes: List<Biome>) {
    private val biomeMobList: Map<BiomeType, List<MobDAO>> = biomes.map {
        Pair(it.biomeType, it.mobs)
    }.toMap()

    init {
        biomes.forEach {
            biomeMobList.plus(Pair(it.biomeType, it.mobs))
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

    fun generateMobResets(rooms: List<RoomDAO>): List<MobReset> {
        val resets = mutableListOf<MobReset>()
        var resetId = 0
        getRoomsToPopulate(rooms).forEach { room ->
            getRandomBiomeMob(room.biome)?.let { mob ->
                resetId++
                resets.add(
                    MobReset(
                        resetId,
                        mob.id.value,
                        room.id.value,
                        (getMaxInRoom(mob) - getMaxInRoomModifier(mob)).coerceAtLeast(1),
                        getMaxInWorld(mob)
                    )
                )
            }
        }
        return resets
    }

    fun getAllMobs(): List<MobDAO> {
        return biomeMobList.flatMap { it.value }
    }

    private fun biomeHasMobs(biomeType: BiomeType): Boolean {
        return biomeMobList[biomeType]?.isNotEmpty() ?: false
    }

    private fun getRandomBiomeMob(biomeType: BiomeType): MobDAO? {
        return biomeMobList[biomeType]?.random()
    }

    private fun getRoomsToPopulate(rooms: List<RoomDAO>): List<RoomDAO> {
        return rooms.filter {
            it.biome.isSurface() && biomeHasMobs(it.biome)
        }.filter {
            coinFlip()
        }
    }
}
