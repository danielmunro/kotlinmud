package kotlinmud.service

import kotlinmud.item.ItemOwner

class ItemService(private val items: MutableList<ItemOwner> = mutableListOf()) {
    fun countItemsById(id: Int): Int {
        return items.filter { it.item.id == id }.size
    }

    fun add(item: ItemOwner) {
        items.add(item)
    }

    fun decrementDecayTimer() {
        items.removeIf {
            if (it.item.decayTimer > 0) {
                it.item.decayTimer -= 1
                return@removeIf it.item.decayTimer == 0
            }
            return@removeIf false
        }
    }
}
