package kotlinmud.generator

import kotlinmud.biome.type.Biome
import kotlinmud.biome.type.BiomeType
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.table.Mobs
import kotlinmud.mob.type.Rarity
import kotlinmud.mob.type.Size
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.table.Rooms
import org.jetbrains.exposed.sql.Random
import org.jetbrains.exposed.sql.and
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
                }.orderBy(Random())).forEach {
                    biome.value.createMobInRoom(it)
                }
            }
        }
    }

    fun getAllMobs(): List<MobDAO> {
        return listOf()
//        return biomeMobList.flatMap { it.value }
    }

    private fun countMobsInRoomByName(mob: MobDAO, room: RoomDAO): Int {
        return transaction {
            Mobs.select {
                Mobs.name eq mob.name and (Mobs.roomId eq room.id)
            }.count()
        }
    }

    private fun countMobsInWorldByName(mob: MobDAO): Int {
        return transaction {
            Mobs.select {
                Mobs.name eq mob.name
            }.count()
        }
    }
}
