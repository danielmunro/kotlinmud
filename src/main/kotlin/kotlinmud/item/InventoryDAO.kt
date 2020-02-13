package kotlinmud.item

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Inventories : IntIdTable()

class InventoryEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<InventoryEntity>(Inventories)

    val items by ItemEntity referrersOn Items.inventory
}
