package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.resource.type.Resource

class GoldOre : Resource {
    override val resourceType = ResourceType.GOLD_ORE
    override val growable = false
    override val maturity: Int? = null
    override val consumesResource = true
    override val toughness = 3

    override fun createProduct(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder().also {
                it.name = "a chunk of rock with hints of gold"
                it.description = "gold ore rock is here"
                it.type = ItemType.GOLD_ORE
            }.build()
        )
    }
}
