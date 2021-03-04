package kotlinmud.item.service

import kotlinmud.action.exception.InvokeException
import kotlinmud.helper.math.dice
import kotlinmud.helper.random.randomAmount
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.repository.decrementAllItemDecayTimers
import kotlinmud.item.repository.removeAllEquipmentForMob
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.model.Mob
import kotlinmud.mob.type.Form

class ItemService {
    private val items = mutableListOf<Item>()
    fun add(item: Item) {
        items.add(item)
    }

    fun getItemGroups(mob: Mob): Map<String, List<Item>> {
        return mob.items.groupBy { it.name }
    }

    fun putItemInContainer(item: Item, container: Item) {
        val containerItems = container.items!!
        if (containerItems.count() >= container.maxItems!! || containerItems.fold(
                0.0,
                { acc: Double, it: Item -> acc + it.weight }
            ) + item.weight > container.maxWeight!!
        ) {
            throw InvokeException("that is too heavy.")
        }
        containerItems.add(item)
    }

    fun decrementDecayTimer() {
        decrementAllItemDecayTimers()
    }

    fun createCorpseFromMob(mob: Mob): Item {
        removeAllEquipmentForMob(mob)
        val corpse = ItemBuilder(this)
            .name("a corpse of $mob")
            .description("a corpse of $mob is here.")
            .level(mob.level)
            .weight(100.0)
            .decayTimer(20)
            .items(mob.items)
            .type(ItemType.CORPSE)
            .material(Material.ORGANIC)
            .build()
        mob.items.clear()
        when (dice(1, 3)) {
            1 -> evaluateMobBodyPartDrop(mob)
            2 -> evaluateMobItemDrops(mob)
        }
        return corpse
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
        return ItemBuilder(this)
            .name("a patch of leather")
            .description("a patch of leather is here.")
            .type(ItemType.LEATHER)
            .material(Material.ORGANIC)
            .weight(0.2)
            .worth(20)
            .build()
    }

    private fun createLargeFang(): Item {
        return ItemBuilder(this)
            .name("a vicious fang")
            .description("a large, vicious fang is lying here.")
            .type(ItemType.OTHER)
            .material(Material.ORGANIC)
            .weight(2.0)
            .worth(1)
            .build()
    }

    private fun createScale(): Item {
        return ItemBuilder(this)
            .name("a scale")
            .description("a scale from a reptile has been left here.")
            .type(ItemType.OTHER)
            .material(Material.ORGANIC)
            .weight(0.1)
            .worth(1)
            .build()
    }

    private fun createSmallFang(): Item {
        return ItemBuilder(this)
            .name("a small fang")
            .description("a small, sharp fang is lying here.")
            .type(ItemType.OTHER)
            .material(Material.ORGANIC)
            .weight(0.1)
            .worth(1)
            .build()
    }

    private fun createBlob(): Item {
        return ItemBuilder(this)
            .name("a blob")
            .description("a small blob is here.")
            .type(ItemType.BLOB)
            .material(Material.ORGANIC)
            .weight(3.0)
            .worth(10)
            .build()
    }

    private fun createThread(): Item {
        return ItemBuilder(this)
            .name("a thin white thread")
            .description("a thin white thread bundle is here.")
            .type(ItemType.THREAD)
            .material(Material.TEXTILE)
            .weight(0.1)
            .worth(1)
            .build()
    }

    private fun createFeather(mob: Mob): Item {
        return ItemBuilder(this)
            .name("$mob's feather")
            .description("a feather plucked unceremoniously from $mob is here.")
            .type(ItemType.FEATHER)
            .material(Material.ORGANIC)
            .weight(0.0)
            .worth(1)
            .build()
    }

    private fun createBrains(mob: Mob): Item {
        return ItemBuilder(this)
            .name("brains of $mob")
            .description("the brains of $mob have been unceremoniously splashed on the ground.")
            .type(ItemType.ORGANS)
            .material(Material.ORGANIC)
            .weight(2.5)
            .worth(1)
            .build()
    }

    private fun createEntrails(): Item {
        return ItemBuilder(this)
            .name("bloody entrails")
            .description("bloody entrails are dashed across the ground.")
            .type(ItemType.ORGANS)
            .material(Material.ORGANIC)
            .weight(5.0)
            .worth(1)
            .build()
    }

    private fun createHeart(mob: Mob): Item {
        return ItemBuilder(this)
            .name("a heart")
            .description("the heart of $mob is here.")
            .type(ItemType.ORGANS)
            .material(Material.ORGANIC)
            .weight(2.0)
            .worth(1)
            .build()
    }

    private fun createLiver(): Item {
        return ItemBuilder(this)
            .name("a liver")
            .description("a liver has been sliced clean from its corpse.")
            .type(ItemType.ORGANS)
            .material(Material.ORGANIC)
            .weight(2.0)
            .worth(1)
            .build()
    }

    private fun createTail(mob: Mob): Item {
        return ItemBuilder(this)
            .name("a tail")
            .description("the tail of $mob has been sliced off.")
            .type(ItemType.ORGANS)
            .material(Material.ORGANIC)
            .weight(1.0)
            .worth(1)
            .build()
    }
}
