package kotlinmud.item.type

import kotlinmud.item.model.Item

interface HasInventory {
    val items: MutableList<Item>
    val maxItems: Int?
    val maxWeight: Int?
}
