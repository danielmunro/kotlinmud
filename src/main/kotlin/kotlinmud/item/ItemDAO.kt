package kotlinmud.item

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Items: IntIdTable() {
    val name = varchar("name", 50)
    val description = text("description")
    val inventory = reference("inventory", Inventories)
}

class Item(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<Item>(
        Items
    )

    var name by Items.name
    var description by Items.description
    val inventory by Inventory referencedOn Items.inventory

}
