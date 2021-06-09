package kotlinmud.world.factory

import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.type.Drink
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.world.service.AreaBuilderService

fun createAmberAle(areaBuilderService: AreaBuilderService): ItemBuilder {
    return areaBuilderService.itemBuilder(
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

fun createPorter(areaBuilderService: AreaBuilderService): ItemBuilder {
    return areaBuilderService.itemBuilder(
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

fun createIPA(areaBuilderService: AreaBuilderService): ItemBuilder {
    return areaBuilderService.itemBuilder(
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
