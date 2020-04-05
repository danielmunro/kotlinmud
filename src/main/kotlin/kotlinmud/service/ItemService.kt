package kotlinmud.service

import kotlinmud.item.Item

class ItemService(private val items: MutableList<Item> = mutableListOf()) {
    fun getItemsById(id: Int): List<Item> {
        return items.filter { it.id == id }
    }

    fun add(item: Item) {
        items.add(item)
    }

    fun decrementDecayTimer() {
        items.removeIf {
            if (it.decayTimer > 0) {
                it.decayTimer -= 1
                return@removeIf it.decayTimer == 0
            }
            return@removeIf false
        }
    }
}
