package kotlinmud.item

import kotlinmud.attributes.model.Attributes
import kotlinmud.item.model.ItemBuilder
import kotlinmud.item.type.Drink
import kotlinmud.item.type.Food
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position

fun itemBuilder(id: Int, name: String): ItemBuilder {
    return ItemBuilder()
        .id(id)
        .name(name)
        .worth(0)
        .level(1)
        .weight(1.0)
        .attributes(Attributes())
        .material(Material.ORGANIC)
        .position(Position.NONE)
        .affects(mutableListOf())
        .drink(Drink.NONE)
        .food(Food.NONE)
        .quantity(0)
}
