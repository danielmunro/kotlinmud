package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.resource.type.Resource

class DiamondOre : Resource {
    override val resourceType = ResourceType.DIAMOND_ORE
    override val growable = false
    override val maturity: Int? = null
    override val consumesResource = true
    override val toughness = 4

    override fun createProduct(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder(
                "a chunk of diamond ore",
                "a chunk of ore glitters here."
            ).also {
                it.type = ItemType.DIAMOND
                it.material = Material.DIAMOND
                it.worth = 15
            }.build()
        )
    }
}
