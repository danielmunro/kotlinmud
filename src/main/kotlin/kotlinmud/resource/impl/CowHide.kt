package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.resource.type.Resource

class CowHide : Resource {
    override val resourceType = ResourceType.ANIMAL_HIDE
    override val growable = false
    override val maturity: Int? = null
    override val consumesResource = true
    override val toughness = 3

    override fun createProduct(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder(
                "a length of leather",
                "a length of leather",
                4.0,
            ).also {
                it.type = ItemType.LEATHER
                it.material = Material.LEATHER
            }.build()
        )
    }
}
