package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class DiamondAxeRecipe : Recipe {
    override val name = "diamond axe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.DIAMOND, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder().also {
                it.name = "a diamond axe"
                it.description = "a diamond axe is here."
                it.type = ItemType.EQUIPMENT
                it.material = Material.DIAMOND
                it.position = Position.WEAPON
                it.damageType = DamageType.SLASH
                it.attackVerb = "chop"
                it.attributes = mapOf(
                    Pair(Attribute.HIT, 3),
                    Pair(Attribute.DAM, 4),
                )
            }.build()
        )
    }
}
