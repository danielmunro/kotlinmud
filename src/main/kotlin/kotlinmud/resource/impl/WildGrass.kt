package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.helper.math.dice
import kotlinmud.helper.random.randomAmount
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.resource.type.Resource

class WildGrass : Resource {
    override val resourceType = ResourceType.BRUSH
    override val growable = true
    override val maturity = 2
    override val consumesResource = true
    override val toughness = 1

    override fun createProduct(itemService: ItemService): List<Item> {
        val itemBuilder = ItemBuilder(itemService)
            .name("small green seeds")
            .description("a handful of small green seeds are here")
            .type(ItemType.GRASS_SEED)
            .material(Material.ORGANIC)
        return randomAmount(3 + dice(1, maturity)) {
            itemBuilder.build()
        }
    }
}
