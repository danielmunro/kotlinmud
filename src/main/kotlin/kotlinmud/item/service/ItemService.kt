package kotlinmud.item.service

import kotlinmud.action.exception.InvokeException
import kotlinmud.helper.math.dice
import kotlinmud.helper.random.randomAmount
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.model.Mob
import kotlinmud.mob.type.Form

class ItemService {
    private val items = mutableListOf<Item>()

    fun builder(): ItemBuilder {
        return ItemBuilder(this)
    }

    fun add(item: Item) {
        items.add(item)
    }

    fun findByCanonicalId(id: ItemCanonicalId): List<Item> {
        return items.filter { it.canonicalId == id }
    }

    fun putItemInContainer(item: Item, container: Item) {
        val containerItems = container.items!!
        if (containerItems.count() >= container.maxItems!! || containerItems.fold(
                0.0,
                { acc: Double, it: Item -> acc + it.weight }
            ) + item.weight > container.maxWeight!!
        ) {
            throw InvokeException("that is too heavy.")
        }
        containerItems.add(item)
    }

    fun decrementDecayTimer() {
        items.removeIf {
            if (it.decayTimer != null) {
                it.decayTimer = it.decayTimer!! - 1
            }
            it.decayTimer != null && it.decayTimer!! <= 0
        }
    }
}
