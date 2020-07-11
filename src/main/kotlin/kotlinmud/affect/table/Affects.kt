package kotlinmud.affect.table

import kotlinmud.attributes.table.Attributes
import kotlinmud.item.table.Items
import kotlinmud.mob.table.Mobs
import org.jetbrains.exposed.dao.IntIdTable

object Affects : IntIdTable() {
    val type = varchar("affect", 50)
    val decay = integer("decay").nullable()
    val timeout = integer("timeout").nullable()
    val mobId = reference("mobId", Mobs)
    val itemId = reference("itemId", Items)
    val attributesId = reference("atttributesId", Attributes)
}
