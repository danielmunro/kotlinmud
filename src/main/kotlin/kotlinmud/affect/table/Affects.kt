package kotlinmud.affect.table

import kotlinmud.attributes.table.Attributes
import kotlinmud.item.table.Items
import kotlinmud.mob.table.Mobs
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Affects : IntIdTable() {
    val type = varchar("affect", 50)
    val timeout = integer("timeout").nullable()
    val mobId = reference("mobId", Mobs).nullable()
    val itemId = reference("itemId", Items, ReferenceOption.CASCADE).nullable()
    val attributesId = reference("attributesId", Attributes).nullable()
}
