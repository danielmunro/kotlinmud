package kotlinmud.world.factory

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.world.service.AreaBuilderService

fun createCureLightPotion(areaBuilderService: AreaBuilderService): ItemBuilder {
    return areaBuilderService.itemBuilder(
        "a potion of cure light",
        "a potion of cure light is here",
        0.2,
    ).also {
        it.spells = listOf(SkillType.CURE_LIGHT)
        it.level = 8
        it.material = Material.ORGANIC
        it.type = ItemType.POTION
        it.worth = 100
    }
}

fun createCurePoisonPotion(areaBuilderService: AreaBuilderService): ItemBuilder {
    return areaBuilderService.itemBuilder(
        "a potion of cure poison",
        "a potion of cure poison is here",
        0.2,
    ).also {
        it.spells = listOf(SkillType.CURE_POISON)
        it.level = 5
        it.material = Material.ORGANIC
        it.type = ItemType.POTION
        it.worth = 20
    }
}

fun createCureBlindnessPotion(areaBuilderService: AreaBuilderService): ItemBuilder {
    return areaBuilderService.itemBuilder(
        "a potion of cure blindness",
        "a potion of cure blindness is here",
        0.2,
    ).also {
        it.spells = listOf(SkillType.CURE_BLINDNESS)
        it.level = 5
        it.material = Material.ORGANIC
        it.type = ItemType.POTION
        it.worth = 50
    }
}

fun createRemoveCursePotion(areaBuilderService: AreaBuilderService): ItemBuilder {
    return areaBuilderService.itemBuilder(
        "a potion of remove curse",
        "a potion of remove curse is here",
        0.2,
    ).also {
        it.spells = listOf(SkillType.REMOVE_CURSE)
        it.level = 5
        it.material = Material.ORGANIC
        it.type = ItemType.POTION
        it.worth = 80
    }
}

fun createHastePotion(areaBuilderService: AreaBuilderService): ItemBuilder {
    return areaBuilderService.itemBuilder(
        "a potion of haste",
        "a potion of haste is here",
        0.2,
    ).also {
        it.spells = listOf(SkillType.HASTE)
        it.level = 10
        it.material = Material.ORGANIC
        it.type = ItemType.POTION
        it.worth = 125
    }
}
