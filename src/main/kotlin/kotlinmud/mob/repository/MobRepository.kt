package kotlinmud.mob.repository

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.table.Mobs
import kotlinmud.mob.type.Disposition
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun findPlayerMobs(): List<MobDAO> {
    return transaction {
        MobDAO.wrapRows(
            Mobs.select {
                Mobs.isNpc eq false
            }
        ).toList()
    }
}

fun findMobsForRoom(room: RoomDAO): List<MobDAO> {
    return transaction {
        MobDAO.wrapRows(
            Mobs.select {
                Mobs.roomId eq room.id
            }
        ).toList()
    }
}

fun findDeadMobs(): List<MobDAO> {
    return transaction {
        MobDAO.wrapRows(
            Mobs.select {
                Mobs.disposition eq Disposition.DEAD.toString()
            }
        ).toList()
    }
}
