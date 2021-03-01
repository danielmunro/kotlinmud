package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.helper.random.randomAmount
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.resource.type.Resource

class Watermelon : Resource {
    override val resourceType = ResourceType.WATERMELON
    override val maturity = 4
    override val growable = false
    override val consumesResource = true
    override val toughness = 3

    override fun createProduct(itemService: ItemService): List<Item> {
        val itemBuilder = ItemBuilder(itemService)
            .name("a juicy slice of watermelon")
            .description("a juicy slice of watermelon is here")
            .food(Food.WATERMELON)
            .material(Material.ORGANIC)
            .type(ItemType.FOOD)
        return randomAmount(4) {
            itemBuilder.build()
        }
    }
}
