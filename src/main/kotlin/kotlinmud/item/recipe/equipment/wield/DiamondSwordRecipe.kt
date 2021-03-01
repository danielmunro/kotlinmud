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

class DiamondSwordRecipe : Recipe {
    override val name = "diamond sword"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.DIAMOND, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
                ItemBuilder(itemService)
                        .name("a diamond sword")
                        .description("a diamond sword is here.")
                        .type(ItemType.EQUIPMENT)
                        .position(Position.WEAPON)
                        .material(Material.DIAMOND)
                        .damageType(DamageType.SLASH)
                        .attackVerb("slash")
                        .attributes(AttributesDAO.new {
                            hit = 5
                            dam = 6
                        })
                        .build()
        )
    }
}
