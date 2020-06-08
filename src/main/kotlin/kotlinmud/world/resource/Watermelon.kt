package kotlinmud.world.resource

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemBuilderBuilder
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.random.randomAmount

class Watermelon : Resource {
    override val resourceType: ResourceType = ResourceType.WATERMELON
    override val growable: Boolean = false
    override val toughness: Int = 3

    override fun createProduct(builder: ItemBuilderBuilder): List<Item> {
        return randomAmount(4) { createItem(builder) }
    }

    private fun createItem(builder: ItemBuilderBuilder): Item {
        return builder()
            .name("a juicy slice of watermelon")
            .description("a juicy slice of watermelon is here")
            .food(Food.WATERMELON)
            .material(Material.ORGANIC)
            .type(ItemType.FOOD)
            .build()
    }
}
