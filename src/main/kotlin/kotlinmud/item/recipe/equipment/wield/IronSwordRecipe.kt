package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class IronSwordRecipe : Recipe {
    override val name = "iron sword"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.IRON_INGOT, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder().also {
                it.name = "an iron sword"
                it.description = "an iron sword is here."
                it.type = ItemType.EQUIPMENT
                it.material = Material.IRON
                it.position = Position.WEAPON
                it.damageType = DamageType.SLASH
                it.attackVerb = "slash"
                it.attributes = mapOf(
                    Pair(Attribute.HIT, 2),
                    Pair(Attribute.DAM, 3),
                )
            }.build()
        )
    }
}
