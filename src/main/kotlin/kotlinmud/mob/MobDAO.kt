package kotlinmud.mob

import kotlinmud.db.enum.DispositionPGEnum
import kotlinmud.item.Inventories
import kotlinmud.item.Inventory
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Mobs: IntIdTable() {
    val name = varchar("name", 50)
    val description = text("description")
    val disposition = customEnumeration(
        "disposition",
        "DispositionEnum",
        { Disposition.values()[it as Int] },
        { DispositionPGEnum("DispositionEnum", it) })
    val inventory = reference("inventory", Inventories)
}

class Mob(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<Mob>(
        Mobs
    )

    var name by Mobs.name
    var description by Mobs.description
    var disposition by Mobs.disposition
    var inventory by Inventory referencedOn Mobs.inventory
}
