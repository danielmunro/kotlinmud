package kotlinmud.item

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Inventories: IntIdTable() {}

class Inventory(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<Inventory>(Inventories)

    val items by Item referrersOn Items.inventory
}