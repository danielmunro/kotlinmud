package kotlinmud.loader.mapper

import kotlinmud.item.Item
import kotlinmud.loader.model.ItemModel

class ItemMapper(private val items: List<ItemModel>) {
    fun map(): List<Item> {
        var i = 1
        return items.map {
            Item(
                i++,
                it.name,
                it.description,
                it.weight,
                it.attributes,
                it.material,
                it.position
            )
        }
    }
}
