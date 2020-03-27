package kotlinmud.item

import kotlinmud.attributes.Attributes

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
