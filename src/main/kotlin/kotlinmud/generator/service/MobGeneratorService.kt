package kotlinmud.generator.service

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

    fun respawnMobs() {
        biomeMobList.entries.forEach { biome ->
            transaction {
                RoomDAO.wrapRows(
                    Rooms.select {
                        Rooms.biome eq biome.key.toString()
                    }.limit(100)
                ).forEach {
                    if (countMobsInRoom(it) < MAX_MOBS_PER_ROOM && biome.value.mobs.isNotEmpty()) {
                        biome.value.mobs.random().invoke(it)
                    }
                }
            }
        }
    }

    private fun countMobsInRoom(room: RoomDAO): Int {
        return Mobs.select { Mobs.roomId eq room.id }.count()
    }
}
