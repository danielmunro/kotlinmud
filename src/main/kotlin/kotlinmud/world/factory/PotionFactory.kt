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
        100,
    ).also {
        it.spells = listOf(SkillType.CURE_LIGHT)
        it.level = 8
        it.material = Material.ORGANIC
        it.type = ItemType.POTION
    }
}

fun createCurePoisonPotion(areaBuilderService: AreaBuilderService): ItemBuilder {
    return areaBuilderService.itemBuilder(
        "a potion of cure poison",
        "a potion of cure poison is here",
        0.2,
        120,
    ).also {
        it.spells = listOf(SkillType.CURE_POISON)
        it.level = 5
        it.material = Material.ORGANIC
        it.type = ItemType.POTION
    }
}

fun createCureBlindnessPotion(areaBuilderService: AreaBuilderService): ItemBuilder {
    return areaBuilderService.itemBuilder(
        "a potion of cure blindness",
        "a potion of cure blindness is here",
        0.2,
        120,
    ).also {
        it.spells = listOf(SkillType.CURE_BLINDNESS)
        it.level = 5
        it.material = Material.ORGANIC
        it.type = ItemType.POTION
    }
}

fun createRemoveCursePotion(areaBuilderService: AreaBuilderService): ItemBuilder {
    return areaBuilderService.itemBuilder(
        "a potion of remove curse",
        "a potion of remove curse is here",
        0.2,
        160,
    ).also {
        it.spells = listOf(SkillType.REMOVE_CURSE)
        it.level = 5
        it.material = Material.ORGANIC
        it.type = ItemType.POTION
    }
}

fun createHastePotion(areaBuilderService: AreaBuilderService): ItemBuilder {
    return areaBuilderService.itemBuilder(
        "a potion of haste",
        "a potion of haste is here",
        0.2,
        225,
    ).also {
        it.spells = listOf(SkillType.HASTE)
        it.level = 10
        it.material = Material.ORGANIC
        it.type = ItemType.POTION
    }
}
