package kotlinmud.mob.dao

import kotlinmud.mob.fight.type.FightStatus
import kotlinmud.mob.table.Fights
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.transactions.transaction

class FightDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FightDAO>(Fights)

    var mob1 by MobDAO referencedOn Fights.mob1
    var mob2 by MobDAO referencedOn Fights.mob2
    var winner by MobDAO optionalReferencedOn Fights.winner
    var status by Fights.status.transform(
        { it.toString() },
        { FightStatus.valueOf(it) }
    )

    fun isOver(): Boolean {
        return status == FightStatus.OVER
    }

    fun getOpponentFor(mob: MobDAO): MobDAO? {
        return transaction {
            when (mob) {
                mob1 -> mob2
                mob2 -> mob1
                else -> null
            }
        }
    }
}
