package kotlinmud.loader.mapper

import kotlinmud.item.Item
import kotlinmud.loader.model.ItemModel

class ItemMapper(private val items: List<ItemModel>) {
    fun map(): List<Item> {
        var i = 1
        return items.map {
            Item.Builder(i++, it.name)
                .setDescription(it.description)
                .setValue(it.value)
                .setLevel(it.level)
                .setWeight(it.weight)
                .setAttributes(it.attributes)
                .setMaterial(it.material)
                .setPosition(it.position)
                .build()
        }
    }
}
