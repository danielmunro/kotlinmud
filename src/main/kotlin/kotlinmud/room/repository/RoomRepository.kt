package kotlinmud.room.repository

import kotlinmud.biome.type.BiomeType
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.table.Rooms
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun findJungleRooms(): List<RoomDAO> {
    return transaction {
        RoomDAO.wrapRows(
            Rooms.select { Rooms.biome eq BiomeType.JUNGLE.toString() }
        ).toList()
    }
}
