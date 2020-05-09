package kotlinmud.service

import kotlin.streams.toList
import kotlinmud.affect.AffectType
import kotlinmud.item.HasInventory
import kotlinmud.item.Item
import kotlinmud.item.ItemOwner
import kotlinmud.string.matches

class ItemService(private val items: MutableList<ItemOwner> = mutableListOf()) {
    fun countItemsById(id: Int): Int {
        return items.filter { it.item.id == id }.size
    }

    fun add(item: ItemOwner) {
        items.add(item)
    }

    fun findByOwner(hasInventory: HasInventory, input: String): Item? {
        val item = items.find { it.owner == hasInventory && matches(it.item.name, input) }?.item
        if (item?.affects()?.findByType(AffectType.INVISIBILITY) == null) {
            return item
        }
        return null
    }

    fun findAllByOwner(hasInventory: HasInventory): List<Item> {
        return items.stream().filter { it.owner == hasInventory }.map { it.item }.toList()
    }

    fun getItemGroups(hasInventory: HasInventory): Map<Int, List<Item>> {
        return findAllByOwner(hasInventory).groupBy { it.id }
    }

    fun changeItemOwner(item: Item, hasInventory: HasInventory) {
        items.find { it.item == item }?.let {
            it.owner = hasInventory
        } ?: items.add(ItemOwner(item, hasInventory))
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

    fun destroy(item: Item) {
        items.removeIf { it.item == item }
    }

    fun transferAllItems(from: HasInventory, to: HasInventory) {
        items.forEach {
            if (it.owner == from) {
                it.owner = to
            }
        }
    }
}
