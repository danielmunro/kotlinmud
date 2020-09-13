package kotlinmud.mob.repository

import kotlinmud.mob.dao.FightDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.type.FightStatus
import kotlinmud.mob.table.Fights
import kotlinmud.mob.table.Mobs
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun findFights(): List<FightDAO> {
    return transaction {
        FightDAO.wrapRows(Fights.selectAll()).toList()
    }
}

fun findFinishedFights(): List<FightDAO> {
    return transaction {
        FightDAO.wrapRows(
            (Fights innerJoin Mobs).select {
                Fights.status eq FightStatus.FIGHTING.toString() and (Mobs.hp less 0)
            }
        ).toList()
    }
}

fun deleteFinishedFights() {
    transaction {
        Fights.deleteWhere(null as Int?, null as Int?) {
            Fights.status eq FightStatus.OVER.toString()
        }
    }
}

fun findFightForMob(mob: MobDAO): FightDAO? {
    return transaction {
        Fights.select {
            Fights.mob1 eq mob.id or (Fights.mob2 eq mob.id)
        }.firstOrNull()?.let {
            FightDAO.wrapRow(it)
        }
    }
}
