package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
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
            itemService.builder().also {
                it.name = "a chunk of rock with hints of iron"
                it.description = "iron ore rock is here"
                it.type = ItemType.IRON_ORE
                it.material = Material.IRON
                it.weight = 5.0
            }.build()
        )
    }
}
