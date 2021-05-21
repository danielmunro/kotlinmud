package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.helper.random.randomAmount
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.resource.type.Resource

class BlackberryBush : Resource {
    override val resourceType = ResourceType.BRUSH
    override val growable = true
    override val maturity = 5
    override val consumesResource = false
    override val toughness = 1

    override fun createProduct(itemService: ItemService): List<Item> {
        val itemBuilder = itemService.builder(
            "a handful of sweet, wild blackberries",
            "a delicious handful of sun-ripened blackberries are here."
        ).also {
            it.type = ItemType.FOOD
            it.material = Material.ORGANIC
        }
        return randomAmount(3) {
            itemBuilder.build()
        }
    }
}
