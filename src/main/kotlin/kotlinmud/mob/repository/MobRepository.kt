package kotlinmud.mob.repository

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.table.Mobs
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.JobType
import kotlinmud.room.dao.RoomDAO
import kotlinmud.type.CanonicalId
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun findMobById(id: Int): MobDAO {
    return transaction {
        MobDAO.wrapRow(
            Mobs.select { Mobs.id eq id }.first()
        )
    }
}

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

fun findMobsWantingToMoveOnTick(): List<MobDAO> {
    return transaction {
        MobDAO.wrapRows(
            Mobs.select {
                Mobs.isNpc eq true and
                    (
                        Mobs.job eq JobType.SCAVENGER.toString() or
                            (
                                Mobs.job eq JobType.FODDER.toString() or
                                    (Mobs.job eq JobType.PATROL.toString())
                                )
                        )
            }
        ).toList()
    }
}

fun findMobInRoomWithJobType(room: RoomDAO, job: JobType): MobDAO? {
    return transaction {
        Mobs.select {
            Mobs.roomId eq room.id and (Mobs.job eq job.toString())
        }.firstOrNull()?.let { MobDAO.wrapRow(it) }
    }
}

fun findMobsByJobType(job: JobType): List<MobDAO> {
    return transaction {
        MobDAO.wrapRows(
            Mobs.select { Mobs.job eq job.toString() }
        ).toList()
    }
}

fun findMobByCanonicalId(id: CanonicalId): MobDAO {
    return transaction {
        MobDAO.wrapRow(
            Mobs.select {
                Mobs.canonicalId eq id.toString()
            }.first()
        )
    }
}
