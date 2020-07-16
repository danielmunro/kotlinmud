package kotlinmud.affect.table

import kotlinmud.attributes.table.Attributes
import kotlinmud.item.table.Items
import kotlinmud.mob.table.Mobs
import org.jetbrains.exposed.dao.IntIdTable

object Affects : IntIdTable() {
    val type = varchar("affect", 50)
    val timeout = integer("timeout").nullable()
    val mobId = reference("mobId", Mobs).nullable()
    val itemId = reference("itemId", Items).nullable()
    val attributesId = reference("attributesId", Attributes).nullable()
}
