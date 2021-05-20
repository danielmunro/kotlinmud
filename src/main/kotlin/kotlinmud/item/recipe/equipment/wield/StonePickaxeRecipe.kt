package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe

class StonePickaxeRecipe : Recipe {
    override val name = "stone pickaxe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.COBBLESTONE, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder().also {
                it.name = "a stone pick axe"
                it.description = "a stone pick axe is here."
                it.type = ItemType.EQUIPMENT
                it.material = Material.STONE
                it.position = Position.WEAPON
                it.attackVerb = "stab"
                it.attributes = mapOf(
                    Pair(Attribute.HIT, 1),
                    Pair(Attribute.DAM, 2),
                )
            }.build()
        )
    }
}
