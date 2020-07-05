package kotlinmud.world.resource

import kotlinmud.biome.type.ResourceType
import kotlinmud.helper.random.randomAmount
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemBuilderBuilder
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material

class Watermelon : Resource {
    override val resourceType: ResourceType = ResourceType.WATERMELON
    override val growable: Boolean = false
    override val toughness: Int = 3

    override fun createProduct(): List<ItemDAO> {
        return randomAmount(4) { createItem() }
    }

    private fun createItem(): ItemDAO {
        return ItemDAO.new {
            name = "a juicy slice of watermelon"
            description = "a juicy slice of watermelon is here"
            food = Food.WATERMELON
            material = Material.ORGANIC
            type = ItemType.FOOD
        }
    }
}
