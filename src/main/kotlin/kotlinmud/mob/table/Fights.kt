package kotlinmud.mob.table

import kotlinmud.mob.fight.type.FightStatus
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Fights : IntIdTable() {
    val status = varchar("status", 250).default(FightStatus.FIGHTING.toString())
    val mob1 = reference("mob1", Mobs, ReferenceOption.CASCADE)
    val mob2 = reference("mob2", Mobs, ReferenceOption.CASCADE)
    val winner = reference("winner", Mobs).nullable()
}
