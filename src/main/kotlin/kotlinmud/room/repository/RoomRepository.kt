package kotlinmud.room.repository

import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.ResourceType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.table.Mobs
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.table.Resources
import kotlinmud.room.table.Rooms
import org.jetbrains.exposed.sql.Join
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.asLiteral
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun insertGrassResource() {
    transaction {
        addLogger(StdOutSqlLogger)
        val complex = Join(
            Rooms, Resources,
            joinType = JoinType.LEFT,
            additionalConstraint = { Resources.type eq ResourceType.BRUSH.toString() }
        )
        Resources.insert(
            complex.slice(
                Resources.type.asLiteral(ResourceType.BRUSH),
                Resources.name.asLiteral("grass"),
                Resources.maturity.asLiteral(0),
                Resources.maturesAt.asLiteral(4),
                Resources.isPlanted.asLiteral(true),
                Rooms.id
            ).select {
                Rooms.substrate eq SubstrateType.DIRT.toString() and (Resources.id.isNull())
            }
        )
    }
}

fun findRoomByMobId(mobId: Int): RoomDAO {
    return transaction {
        RoomDAO.wrapRow(
            (Rooms innerJoin Mobs).select {
                Mobs.id eq mobId and (Rooms.id eq Mobs.roomId)
            }.first()
        )
    }
}

fun findStartRoom(): RoomDAO? {
    return transaction {
        Rooms.select {
            (
                Rooms.biome eq BiomeType.ARBOREAL.toString() or
                    (Rooms.biome eq BiomeType.PLAINS.toString()) or
                    (Rooms.biome eq BiomeType.JUNGLE.toString())
                ) and
                (
                    (Rooms.northId.isNotNull()) or
                        (Rooms.southId.isNotNull()) or
                        (Rooms.eastId.isNotNull()) or
                        (Rooms.westId.isNotNull()) or
                        (Rooms.upId.isNotNull()) or
                        (Rooms.downId.isNotNull())
                    )
        }.firstOrNull()?.let {
            RoomDAO.wrapRow(it)
        }
    }
}

fun findRoomById(id: Int): RoomDAO {
    return transaction {
        RoomDAO.wrapRow(
            Rooms.select { Rooms.id eq id }.first()
        )
    }
}

fun findJungleRooms(): List<RoomDAO> {
    return transaction {
        RoomDAO.wrapRows(
            Rooms.select { Rooms.biome eq BiomeType.JUNGLE.toString() }
        ).toList()
    }
}
