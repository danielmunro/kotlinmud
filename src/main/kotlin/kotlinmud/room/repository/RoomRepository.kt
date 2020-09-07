package kotlinmud.room.repository

import kotlinmud.biome.type.BiomeType
import kotlinmud.mob.table.Mobs
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.table.Rooms
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun findRoomByMobId(mobId: Int): RoomDAO {
    return transaction {
        RoomDAO.wrapRow(
            (Rooms innerJoin Mobs).select {
                Mobs.id eq mobId and (Rooms.id eq Mobs.roomId)
            }.first()
        )
    }
}

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
