package kotlinmud.item.service

import kotlinmud.helper.math.dice
import kotlinmud.helper.random.randomAmount
import kotlinmud.item.model.Item
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.model.Mob
import kotlinmud.mob.type.Form

class CorpseService(private val itemService: ItemService) {
    fun createCorpseFromMob(mob: Mob): Item {
        removeAllEquipmentForMob(mob)
        val corpse = itemService.builder(
            "a corpse of $mob",
            "a corpse of $mob is here."
        ).also {
            it.level = mob.level
            it.weight = 100.0
            it.decayTimer = 20
            it.items = mob.items
            it.type = ItemType.CORPSE
            it.material = Material.ORGANIC
        }.build()
        mob.items.clear()
        when (dice(1, 3)) {
            1 -> evaluateMobBodyPartDrop(mob)
            2 -> evaluateMobItemDrops(mob)
        }
        return corpse
    }

    private fun removeAllEquipmentForMob(mob: Mob) {
        mob.items.addAll(mob.equipped)
        mob.equipped.clear()
    }

    private fun evaluateMobItemDrops(mob: Mob) {
        val room = mob.room
        when (mob.race.form) {
            Form.MAMMAL -> randomAmount(2) { createLeather() }
            Form.REPTILE -> createDropFromReptile(mob)
            Form.DRAGON -> listOf(createLargeFang())
            Form.SPIDER -> listOf(createThread())
            Form.BIRD -> randomAmount(3) { createFeather(mob) }
            Form.SNAKE -> listOf(createSmallFang())
            Form.FISH -> randomAmount(2) { createScale() }
            Form.BLOB -> listOf(createBlob())
            else -> return
        }.forEach {
            room.items.add(it)
        }
    }

    private fun evaluateMobBodyPartDrop(mob: Mob) {
        when (dice(1, 4)) {
            1 -> createBrains(mob)
            2 -> createEntrails()
            3 -> createHeart(mob)
            4 -> createLiver()
        }
    }

    private fun createDropFromReptile(mob: Mob): List<Item> {
        return when (dice(1, 3)) {
            1 -> randomAmount(3) { createScale() }
            2 -> listOf(createTail(mob))
            3 -> listOf(createSmallFang())
            else -> listOf()
        }
    }

    private fun createLeather(): Item {
        return itemService.builder(
            "a patch of leather",
            "a patch of leather is here."
        ).also {
            it.type = ItemType.LEATHER
            it.material = Material.ORGANIC
            it.weight = 0.2
            it.worth = 20
        }.build()
    }

    private fun createLargeFang(): Item {
        return itemService.builder(
            "a vicious fang",
            "a large, vicious fang is lying here."
        ).also {
            it.type = ItemType.OTHER
            it.material = Material.ORGANIC
            it.weight = 2.0
            it.worth = 1
        }.build()
    }

    private fun createScale(): Item {
        return itemService.builder(
            "a scale",
            "a scale from a reptile has been left here."
        ).also {
            it.type = ItemType.OTHER
            it.material = Material.ORGANIC
            it.weight = 0.1
            it.worth = 1
        }.build()
    }

    private fun createSmallFang(): Item {
        return itemService.builder(
            "a small fang",
            "a small, sharp fang is lying here."
        ).also {
            it.type = ItemType.OTHER
            it.material = Material.ORGANIC
            it.weight = 0.1
            it.worth = 1
        }.build()
    }

    private fun createBlob(): Item {
        return itemService.builder(
            "a blob",
            "a small blob is here."
        ).also {
            it.type = ItemType.BLOB
            it.material = Material.ORGANIC
            it.weight = 3.0
            it.worth = 10
        }.build()
    }

    private fun createThread(): Item {
        return itemService.builder(
            "a thin white thread",
            "a thin white thread bundle is here."
        ).also {
            it.type = ItemType.THREAD
            it.material = Material.TEXTILE
            it.weight = 0.1
            it.worth = 1
        }.build()
    }

    private fun createFeather(mob: Mob): Item {
        return itemService.builder(
            "$mob's feather",
            "a feather plucked unceremoniously from $mob is here."
        ).also {
            it.material = Material.ORGANIC
            it.type = ItemType.FEATHER
            it.weight = 0.0
            it.worth = 1
        }.build()
    }

    private fun createBrains(mob: Mob): Item {
        return itemService.builder(
            "brains of $mob",
            "the brains of $mob have been unceremoniously splashed on the ground."
        ).also {
            it.makeOrgans()
            it.weight = 2.5
            it.worth = 1
        }.build()
    }

    private fun createEntrails(): Item {
        return itemService.builder(
            "bloody entrails",
            "bloody entrails are dashed across the ground."
        ).also {
            it.makeOrgans()
            it.weight = 5.0
            it.worth = 1
        }.build()
    }

    private fun createHeart(mob: Mob): Item {
        return itemService.builder(
            "the heart of $mob",
            "the heart of $mob is here."
        ).also {
            it.makeOrgans()
            it.weight = 2.0
            it.worth = 1
        }.build()
    }

    private fun createLiver(): Item {
        return itemService.builder(
            "a liver",
            "a liver has been sliced clean from its corpse."
        ).also {
            it.makeOrgans()
            it.weight = 2.0
            it.worth = 1
        }.build()
    }

    private fun createTail(mob: Mob): Item {
        return itemService.builder(
            "the tail of $mob",
            "the tail of $mob has been sliced off."
        ).also {
            it.makeOrgans()
            it.weight = 1.0
            it.worth = 1
        }.build()
    }
}
