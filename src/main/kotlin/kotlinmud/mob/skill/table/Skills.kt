package kotlinmud.mob.skill.table

import kotlinmud.mob.table.Mobs
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Skills : IntIdTable() {
    val type = varchar("type", 50)
    val level = integer("level")
    val mobId = reference("mobId", Mobs, ReferenceOption.CASCADE)
}
