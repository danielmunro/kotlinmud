package kotlinmud.generator

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.table.Rooms
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

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
                    biome.value.createMobInRoom(it)
                }
            }
        }
    }
}
