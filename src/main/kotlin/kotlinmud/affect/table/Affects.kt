package kotlinmud.affect.table

import kotlinmud.mob.table.Mobs
import org.jetbrains.exposed.sql.Table

object Affects : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val affect = varchar("affect", 50)
    val mobId = (integer("mob_id") references Mobs.id).nullable()
//    val itemId = (integer("item_id") references Items.id).nullable()
}
