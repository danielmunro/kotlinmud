package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.resource.type.Resource

class IronOre : Resource {
    override val resourceType = ResourceType.IRON_ORE
    override val growable = false
    override val maturity: Int? = null
    override val consumesResource = true
    override val toughness = 3

    override fun createProduct(itemService: ItemService): List<Item> {
        return listOf(
            ItemBuilder(itemService)
                .name("a chunk of rock with hints of iron")
                .description("iron ore rock is here")
                .type(ItemType.IRON_ORE)
                .material(Material.IRON)
                .weight(5.0)
                .build()
        )
    }
}
