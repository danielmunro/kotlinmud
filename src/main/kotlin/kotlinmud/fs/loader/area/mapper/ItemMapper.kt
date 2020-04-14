package kotlinmud.fs.loader.area.mapper

import kotlinmud.item.Item
import kotlinmud.item.ItemBuilder

class ItemMapper(private val items: List<ItemBuilder>) {
    fun map(): List<Item> {
        return items.map {
            it.build()
        }
    }
}
