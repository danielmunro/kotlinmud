package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class WoodenPickAxeRecipe : Recipe {
    override val name = "wooden pickaxe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
                ItemBuilder(itemService)
                        .name("a wooden pick axe")
                        .description("a wooden pick axe is here.")
                        .type(ItemType.EQUIPMENT)
                        .material(Material.WOOD)
                        .position(Position.WEAPON)
                        .damageType(DamageType.PIERCE)
                        .attackVerb("stab")
                        .attributes(AttributesDAO.new {
                            hit = 1
                            dam = 1
                        })
                        .build()
        )
    }
}
