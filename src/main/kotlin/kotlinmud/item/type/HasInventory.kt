package kotlinmud.item.type

import kotlinmud.item.dao.ItemDAO
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.SizedIterable

interface HasInventory {
    val id: EntityID<Int>
    val items: SizedIterable<ItemDAO>
}
