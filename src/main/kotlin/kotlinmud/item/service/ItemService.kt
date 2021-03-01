package kotlinmud.item.service

import kotlinmud.action.exception.InvokeException
import kotlinmud.helper.math.dice
import kotlinmud.helper.random.randomAmount
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.factory.createBlob
import kotlinmud.item.factory.createBrains
import kotlinmud.item.factory.createDropFromReptile
import kotlinmud.item.factory.createEntrails
import kotlinmud.item.factory.createFeather
import kotlinmud.item.factory.createHeart
import kotlinmud.item.factory.createLargeFang
import kotlinmud.item.factory.createLeather
import kotlinmud.item.factory.createLiver
import kotlinmud.item.factory.createScale
import kotlinmud.item.factory.createSmallFang
import kotlinmud.item.factory.createThread
import kotlinmud.item.model.Item
import kotlinmud.item.repository.decrementAllItemDecayTimers
import kotlinmud.item.repository.findOneByRoom
import kotlinmud.item.repository.removeAllEquipmentForMob
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.mob.model.Mob
import kotlinmud.mob.type.Form
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

class ItemService {
    private val items = mutableListOf<Item>()
    fun add(item: Item) {
        items.add(item)
    }

    fun findByRoom(room: RoomDAO, input: String): ItemDAO? {
        return findOneByRoom(room, input)
    }

    fun getItemGroups(mob: Mob): Map<ItemCanonicalId?, List<Item>> {
        return mob.items.groupBy { it.canonicalId }
    }

    fun putItemInRoom(item: ItemDAO, room: RoomDAO) {
        transaction {
            item.mobInventory = null
            item.mobEquipped = null
            item.container = null
            item.room = room
        }
    }

    fun putItemInContainer(item: ItemDAO, container: ItemDAO) {
        transaction {
            if (container.items.count() >= container.maxItems!! || container.items.fold(
                    0.0,
                    { acc: Double, it: ItemDAO -> acc + it.weight }
                ) + item.weight > container.maxWeight!!
            ) {
                throw InvokeException("that is too heavy.")
            }
        }
        transaction {
            item.mobInventory = null
            item.mobEquipped = null
            item.room = null
            item.container = container
        }
    }

    fun decrementDecayTimer() {
        decrementAllItemDecayTimers()
    }

    fun createCorpseFromMob(mob: Mob): Item {
        removeAllEquipmentForMob(mob)
        val corpse = ItemBuilder(this@ItemService)
            .name("a corpse of $mob")
            .description("a corpse of $mob is here.")
            .level(mob.level)
            .weight(100.0)
            .decayTimer(20)
            .items(mob.items)
            .build()
        mob.items.clear()
        transaction {
            when (dice(1, 3)) {
                1 -> evaluateMobBodyPartDrop(mob)
                2 -> evaluateMobItemDrops(mob)
            }
        }
        return corpse
    }

    private fun evaluateMobItemDrops(mob: Mob) {
        val room = transaction { mob.room }
        when (mob.race.form) {
            Form.MAMMAL -> randomAmount(2) { createLeather(room) }
            Form.REPTILE -> createDropFromReptile(mob, room)
            Form.DRAGON -> createLargeFang(room)
            Form.SPIDER -> createThread(room)
            Form.BIRD -> randomAmount(3) { createFeather(mob, room) }
            Form.SNAKE -> createSmallFang(room)
            Form.FISH -> randomAmount(2) { createScale(room) }
            Form.BLOB -> createBlob(room)
            else -> return
        }
    }

    private fun evaluateMobBodyPartDrop(mob: Mob) {
        val room = transaction { mob.room }
        when (dice(1, 4)) {
            1 -> createBrains(mob, room)
            2 -> createEntrails(room)
            3 -> createHeart(mob, room)
            4 -> createLiver(room)
        }
    }

    private fun checkItemCount(inventory: HasInventory) {
        transaction {
            val count = inventory.items.count()
            if (inventory.maxItems != null && count + 1 > inventory.maxItems!!) {
                throw InvokeException(
                    if (inventory is Mob)
                        "you cannot carry any more."
                    else
                        "that is full."
                )
            }
        }
    }

    private fun checkWeight(inventory: HasInventory, item: ItemDAO) {
        transaction {
            val weight = inventory.items.fold(0.0) { acc, it -> acc + it.weight }
            val maxWeight = inventory.maxWeight
            if (maxWeight != null && weight + item.weight > maxWeight) {
                throw InvokeException("that is too heavy.")
            }
        }
    }
}
