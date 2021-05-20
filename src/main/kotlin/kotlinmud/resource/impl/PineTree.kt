package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.resource.type.Resource

class PineTree : Resource {
    override val resourceType = ResourceType.PINE_TREE
    override val maturity = 10
    override val growable = true
    override val consumesResource = true
    override val toughness = 2

    override fun createProduct(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder().also {
                it.name = "a pine tree log"
                it.description = "a pine tree log is here"
                it.type = ItemType.WOOD_LOG
            }.build()
        )
    }
}
