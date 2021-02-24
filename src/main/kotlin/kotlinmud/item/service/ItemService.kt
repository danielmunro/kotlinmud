package kotlinmud.item.service

import kotlinmud.action.exception.InvokeException
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.helper.math.dice
import kotlinmud.helper.random.randomAmount
import kotlinmud.helper.string.matches
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
import kotlinmud.item.repository.decrementAllItemDecayTimers
import kotlinmud.item.repository.findOneByRoom
import kotlinmud.item.repository.removeAllEquipmentForMob
import kotlinmud.item.type.HasInventory
import kotlinmud.mob.model.Mob
import kotlinmud.mob.type.Form
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.transactions.transaction

class ItemService {
    fun findByOwner(mob: Mob, input: String): ItemDAO? {
        return mob.items.find { it.name.matches(input) }
    }

    fun findByRoom(room: RoomDAO, input: String): ItemDAO? {
        return findOneByRoom(room, input)
    }

    fun getItemGroups(mob: Mob): Map<EntityID<Int>, List<ItemDAO>> {
        return mob.items.groupBy { it.id }
    }

    fun giveItemToMob(item: ItemDAO, mob: Mob) {
        checkItemCount(mob)
        checkWeight(mob, item)
        mob.items.add(item)
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
//        checkItemCount(container)
//        checkWeight(container, item)
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

    fun createCorpseFromMob(mob: Mob): ItemDAO {
        val item = transaction {
            removeAllEquipmentForMob(mob)
            ItemDAO.new {
                name = "a corpse of $mob"
                description = "a corpse of $mob is here."
                level = mob.level
                weight = 100.0
                decayTimer = 20
                attributes = AttributesDAO.new {}
                room = mob.room
            }
        }
        mob.items.forEach {
            transaction {
                it.container = item
            }
        }
        transaction {
            mob.equipped.forEach {
                it.container = item
            }
        }
        mob.items.clear()
        transaction {
            when (dice(1, 3)) {
                1 -> evaluateMobBodyPartDrop(mob)
                2 -> evaluateMobItemDrops(mob)
            }
        }
        return item
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
