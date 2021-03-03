package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class StoneAxeRecipe : Recipe {
    override val name = "stone axe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.COBBLESTONE, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            ItemBuilder(itemService)
                .name("a stone axe")
                .description("a stone axe is here.")
                .type(ItemType.EQUIPMENT)
                .material(Material.STONE)
                .position(Position.WEAPON)
                .damageType(DamageType.SLASH)
                .attackVerb("chop")
                .attributes(
                        mapOf(
                                Pair(Attribute.HIT, 1),
                                Pair(Attribute.DAM, 2),
                        )
                )
                .build()
        )
    }
}
