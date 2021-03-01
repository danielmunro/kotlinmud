package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.resource.type.Resource

class DiamondOre : Resource {
    override val resourceType = ResourceType.DIAMOND_ORE
    override val growable = false
    override val maturity: Int? = null
    override val consumesResource = true
    override val toughness = 4

    override fun createProduct(itemService: ItemService): List<Item> {
        return listOf(
            ItemBuilder(itemService)
                .name("a diamond")
                .description("a glittering diamond is here.")
                .type(ItemType.DIAMOND)
                .build()
        )
    }
}
