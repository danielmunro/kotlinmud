package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class DiamondPickAxeRecipe : Recipe {
    override val name = "diamond pickaxe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.DIAMOND, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            ItemBuilder(itemService)
                .name("a diamond pick axe")
                .description("a diamond pick axe is here.")
                .type(ItemType.EQUIPMENT)
                .material(Material.DIAMOND)
                .position(Position.WEAPON)
                .damageType(DamageType.PIERCE)
                .attackVerb("stab")
                .attributes(
                    AttributesDAO.new {
                        hit = 3
                        dam = 4
                    }
                )
                .build()
        )
    }
}
