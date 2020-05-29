package kotlinmud.fs.loader.area.mapper

import kotlinmud.item.model.Item
import kotlinmud.item.model.ItemBuilder

class ItemMapper(private val items: List<ItemBuilder>) {
    fun map(): List<Item> {
        return items.map {
            it.build()
        }
    }
}
