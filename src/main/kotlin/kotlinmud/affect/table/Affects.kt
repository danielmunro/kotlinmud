package kotlinmud.affect.table

import kotlinmud.item.table.Items
import kotlinmud.mob.table.Mobs
import org.jetbrains.exposed.dao.IntIdTable

object Affects : IntIdTable() {
    val affect = varchar("affect", 50)
    val mobId = reference("mobId", Mobs)
    val itemId = reference("itemId", Items)
}
