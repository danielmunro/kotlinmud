package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.resource.type.Resource

class CoalOre : Resource {
    override val resourceType = ResourceType.COAL_ORE
    override val growable = false
    override val maturity: Int? = null
    override val consumesResource = true
    override val toughness = 3

    override fun createProduct(itemService: ItemService): List<Item> {
        return listOf(
            ItemBuilder(itemService)
                .name("a lump of coal")
                .description("a lump of coal is here")
                .type(ItemType.COAL_LUMP)
                .build()
        )
    }
}
