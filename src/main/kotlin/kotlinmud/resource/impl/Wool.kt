package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.resource.type.Resource

class Wool : Resource {
    override val resourceType = ResourceType.WOOL
    override val growable = true
    override val maturity = 2
    override val consumesResource = true
    override val toughness = 1

    override fun createProduct(itemService: ItemService): List<Item> {
        val itemBuilder = itemService.builder(
            "a spool of thread",
            "a spool of thread is here",
            2.0,
        ).also {
            it.type = ItemType.THREAD
            it.material = Material.ORGANIC
        }
        return listOf(itemBuilder.build())
    }
}
