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
            "a corpse of $mob is here.",
            20.0,
        ).also {
            it.level = mob.level
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
            "a patch of leather is here.",
            0.3,
        ).also {
            it.type = ItemType.LEATHER
            it.material = Material.ORGANIC
            it.worth = 1
        }.build()
    }

    private fun createLargeFang(): Item {
        return itemService.builder(
            "a vicious fang",
            "a large, vicious fang is lying here.",
            0.1,
        ).also {
            it.type = ItemType.OTHER
            it.material = Material.ORGANIC
            it.worth = 0
        }.build()
    }

    private fun createScale(): Item {
        return itemService.builder(
            "a scale",
            "a scale from a reptile has been left here.",
            0.1,
        ).also {
            it.type = ItemType.OTHER
            it.material = Material.ORGANIC
            it.worth = 0
        }.build()
    }

    private fun createSmallFang(): Item {
        return itemService.builder(
            "a small fang",
            "a small, sharp fang is lying here.",
            0.1,
        ).also {
            it.type = ItemType.OTHER
            it.material = Material.ORGANIC
            it.worth = 0
        }.build()
    }

    private fun createBlob(): Item {
        return itemService.builder(
            "a blob",
            "a small blob is here.",
            0.5,
        ).also {
            it.type = ItemType.BLOB
            it.material = Material.ORGANIC
            it.worth = 0
        }.build()
    }

    private fun createThread(): Item {
        return itemService.builder(
            "a thin white thread",
            "a thin white thread bundle is here.",
            0.1,
        ).also {
            it.type = ItemType.THREAD
            it.material = Material.TEXTILE
            it.worth = 1
        }.build()
    }

    private fun createFeather(mob: Mob): Item {
        return itemService.builder(
            "$mob's feather",
            "a feather plucked unceremoniously from $mob is here.",
            0.0,
        ).also {
            it.material = Material.ORGANIC
            it.type = ItemType.FEATHER
            it.worth = 0
        }.build()
    }

    private fun createBrains(mob: Mob): Item {
        return itemService.builder(
            "brains of $mob",
            "the brains of $mob have been unceremoniously splashed on the ground.",
            2.0,
        ).makeOrgans().also {
            it.worth = 0
        }.build()
    }

    private fun createEntrails(): Item {
        return itemService.builder(
            "bloody entrails",
            "bloody entrails are dashed across the ground.",
            3.0,
        ).makeOrgans().also {
            it.worth = 0
        }.build()
    }

    private fun createHeart(mob: Mob): Item {
        return itemService.builder(
            "the heart of $mob",
            "the heart of $mob is here.",
        ).makeOrgans().also {
            it.worth = 0
        }.build()
    }

    private fun createLiver(): Item {
        return itemService.builder(
            "a liver",
            "a liver has been sliced clean from its corpse.",
            1.5,
        ).makeOrgans().also {
            it.worth = 0
        }.build()
    }

    private fun createTail(mob: Mob): Item {
        return itemService.builder(
            "the tail of $mob",
            "the tail of $mob has been sliced off.",
        ).makeOrgans().also {
            it.worth = 0
        }.build()
    }
}
