package kotlinmud.generator

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.mob.table.Mobs
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.table.Rooms
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

const val MAX_MOBS_PER_ROOM = 4

class MobGeneratorService(biomes: List<Biome>) {
    private val biomeMobList: Map<BiomeType, Biome> = biomes.map {
        Pair(it.biomeType, it)
    }.toMap()

    init {
        biomes.forEach {
            biomeMobList.plus(Pair(it.biomeType, it))
        }
    }

    fun respawnMobs() {
        transaction {
            biomeMobList.entries.forEach { biome ->
                RoomDAO.wrapRows(Rooms.select {
                    Rooms.biome eq biome.key.value
                }).forEach {
                    if (Mobs.select { Mobs.roomId eq it.id }.count() < MAX_MOBS_PER_ROOM) {
                        biome.value.mobs.random().invoke(it)
                    }
                }
            }
        }
    }
}
