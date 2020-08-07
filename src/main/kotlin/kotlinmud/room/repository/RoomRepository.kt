package kotlinmud.room.repository

import kotlinmud.biome.type.BiomeType
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.table.Rooms
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun findStartRoom(): RoomDAO {
    return transaction {
        RoomDAO.wrapRow(
            Rooms.select {
                Rooms.biome eq BiomeType.ARBOREAL.toString() or
                        (Rooms.biome eq BiomeType.PLAINS.toString()) or
                        (Rooms.biome eq BiomeType.JUNGLE.toString())
            }.limit(1)
                .first()
        )
    }
}

fun findRoomById(id: Int): RoomDAO {
    return transaction {
        RoomDAO.wrapRow(
            Rooms.select { Rooms.id eq id }.first()
        )
    }
}
