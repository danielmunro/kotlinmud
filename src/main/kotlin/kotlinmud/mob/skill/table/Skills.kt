package kotlinmud.mob.skill.table

import kotlinmud.mob.table.Mobs
import org.jetbrains.exposed.sql.Table

object Skills : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 50)
    val level = integer("level")
    val mobId = (integer("mob_id") references Mobs.id)
}
