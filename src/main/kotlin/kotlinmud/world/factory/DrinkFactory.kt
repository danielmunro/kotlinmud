package kotlinmud.world.factory

import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Drink
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material

fun createAmberAle(itemService: ItemService): ItemBuilder {
    return itemService.builder(
        "a light amber ale",
        "a light amber ale is here",
        0.2,
    ).also {
        it.affects = listOf(Affect(AffectType.DRUNK, 5))
        it.level = 5
        it.material = Material.ORGANIC
        it.type = ItemType.DRINK
        it.drink = Drink.BEER
        it.worth = 1
    }
}

fun createPorter(itemService: ItemService): ItemBuilder {
    return itemService.builder(
        "a dark porter",
        "a dark porter is here",
        0.2,
    ).also {
        it.affects = listOf(Affect(AffectType.DRUNK, 4))
        it.level = 5
        it.material = Material.ORGANIC
        it.type = ItemType.DRINK
        it.drink = Drink.BEER
        it.worth = 1
    }
}

fun createIPA(itemService: ItemService): ItemBuilder {
    return itemService.builder(
        "an IPA",
        "an IPA is here",
        0.2,
    ).also {
        it.affects = listOf(Affect(AffectType.DRUNK, 6))
        it.level = 5
        it.material = Material.ORGANIC
        it.type = ItemType.DRINK
        it.drink = Drink.BEER
        it.worth = 1
    }
}
