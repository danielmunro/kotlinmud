package kotlinmud.world.factory

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.skill.type.SkillType

fun createCureLightPotion(itemService: ItemService): ItemBuilder {
    return itemService.builder(
        "a potion of cure light",
        "a potion of cure light is here",
        0.2,
    ).also {
        it.spells = listOf(SkillType.CURE_LIGHT)
        it.level = 8
        it.canonicalId = ItemCanonicalId.PotionCureLight
        it.material = Material.ORGANIC
        it.type = ItemType.POTION
        it.worth = 100
    }
}

fun createCurePoisonPotion(itemService: ItemService): ItemBuilder {
    return itemService.builder(
        "a potion of cure poison",
        "a potion of cure poison is here",
        0.2,
    ).also {
        it.spells = listOf(SkillType.CURE_POISON)
        it.level = 5
        it.canonicalId = ItemCanonicalId.PotionCurePoison
        it.material = Material.ORGANIC
        it.type = ItemType.POTION
        it.worth = 20
    }
}

fun createCureBlindnessPotion(itemService: ItemService): ItemBuilder {
    return itemService.builder(
        "a potion of cure blindness",
        "a potion of cure blindness is here",
        0.2,
    ).also {
        it.spells = listOf(SkillType.CURE_BLINDNESS)
        it.level = 5
        it.canonicalId = ItemCanonicalId.PotionCureBlindness
        it.material = Material.ORGANIC
        it.type = ItemType.POTION
        it.worth = 50
    }
}

fun createRemoveCursePotion(itemService: ItemService): ItemBuilder {
    return itemService.builder(
        "a potion of remove curse",
        "a potion of remove curse is here",
        0.2,
    ).also {
        it.spells = listOf(SkillType.REMOVE_CURSE)
        it.level = 5
        it.canonicalId = ItemCanonicalId.PotionRemoveCurse
        it.material = Material.ORGANIC
        it.type = ItemType.POTION
        it.worth = 80
    }
}

fun createHastePotion(itemService: ItemService): ItemBuilder {
    return itemService.builder(
        "a potion of haste",
        "a potion of haste is here",
        0.2,
    ).also {
        it.spells = listOf(SkillType.HASTE)
        it.level = 10
        it.canonicalId = ItemCanonicalId.PotionHaste
        it.material = Material.ORGANIC
        it.type = ItemType.POTION
        it.worth = 125
    }
}
