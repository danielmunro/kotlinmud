package kotlinmud.mob

import kotlinmud.item.Inventories
import kotlinmud.item.Inventory
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Mobs: IntIdTable() {
    val name = varchar("name", 50)
    val description = text("description")
    val inventory = reference("inventory", Inventories)
}

class Mob(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<Mob>(
        Mobs
    )

    var name by Mobs.name
    var description by Mobs.description
    val inventory by Inventory referrersOn Mobs.inventory
}
