package kotlinmud.item.dao

import kotlinmud.item.table.Items
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class ItemDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ItemDAO>(Items)

    var name by Items.name
}
