package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.resource.type.Resource

class Tar : Resource {
    override val resourceType = ResourceType.TAR
    override val growable = false
    override val maturity: Int? = null
    override val consumesResource = true
    override val toughness = 2

    override fun createProduct(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder(
                "a lump of tar",
                "a lump of tar is here, make sure not to touch it!",
                0.5,
            ).also {
                it.type = ItemType.TAR
            }.build()
        )
    }
}
