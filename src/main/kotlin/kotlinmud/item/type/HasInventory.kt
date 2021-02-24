package kotlinmud.item.type

import kotlinmud.item.dao.ItemDAO

interface HasInventory {
    val items: MutableList<ItemDAO>
    val maxItems: Int?
    val maxWeight: Int?
}
