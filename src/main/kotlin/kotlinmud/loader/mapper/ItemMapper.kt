package kotlinmud.loader.mapper

import kotlinmud.item.Item

class ItemMapper(private val items: List<Item.Builder>) {
    fun map(): List<Item> {
        return items.map {
            it.build()
        }
    }
}
