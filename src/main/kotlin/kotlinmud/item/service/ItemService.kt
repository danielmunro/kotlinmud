package kotlinmud.item.service

import kotlinmud.action.exception.InvokeException
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.persistence.model.ItemModel
import kotlinmud.room.model.Area
import kotlinmud.service.BaseService
import java.util.UUID

class ItemService : BaseService() {
    private val items = mutableListOf<Item>()

    fun findItemModels(area: Area): List<ItemModel> {
        return models.filterIsInstance<ItemModel>().filter { it.area == area }
    }

    fun findOne(predicate: (Item) -> Boolean): Item? {
        return items.find(predicate)
    }

    fun builder(name: String, description: String, weight: Double = 0.0, worth: Int = 0): ItemBuilder {
        return ItemBuilder(this).also {
            it.name = name
            it.description = description
            it.weight = weight
            it.worth = worth
        }
    }

    fun getItemCount(): Int {
        return items.size
    }

    fun add(item: Item) {
        items.add(item)
    }

    fun remove(item: Item) {
        items.remove(item)
    }

    fun findById(id: Int): List<Item> {
        return items.filter { it.id == id }
    }

    fun findByCanonicalId(id: UUID): List<Item> {
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
