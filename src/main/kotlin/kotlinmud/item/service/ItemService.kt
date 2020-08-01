package kotlinmud.item.service

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.table.Items
import kotlinmud.item.table.Items.decayTimer
import kotlinmud.item.table.Items.itemId
import kotlinmud.item.table.Items.mobInventoryId
import kotlinmud.item.table.Items.roomId
import kotlinmud.item.type.HasInventory
import kotlinmud.mob.dao.MobDAO
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ItemService {
    companion object {
        fun getColumn(hasInventory: HasInventory): Column<EntityID<Int>?> {
            return when (hasInventory) {
                is MobDAO -> mobInventoryId
                is RoomDAO -> roomId
                is ItemDAO -> itemId
                else -> throw Exception("no has inventory")
            }
        }
    }

    fun findAllByOwner(hasInventory: HasInventory): List<ItemDAO> {
        return transaction {
            ItemDAO.wrapRows(
                Items.select {
                    getColumn(hasInventory) eq hasInventory.id
                }
            ).toList()
        }
    }

    fun findByOwner(mob: MobDAO, input: String): ItemDAO? {
        return transaction {
            ItemDAO.wrapRows(
                Items.select(mobInventoryId eq mob.id and (Items.name like "%$input%"))
            ).firstOrNull()
        }
    }

    fun findByRoom(room: RoomDAO, input: String): ItemDAO? {
        return transaction {
            ItemDAO.wrapRows(
                Items.select(roomId eq room.id and (Items.name like "%$input%"))
            ).firstOrNull()
        }
    }

    fun getItemGroups(mob: MobDAO): Map<EntityID<Int>, List<ItemDAO>> {
        return findAllByOwner(mob).groupBy { it.id }
    }

    fun giveItemToMob(item: ItemDAO, mob: MobDAO) {
        transaction {
            item.mobInventory = mob
            item.room = null
            item.mobEquipped = null
            item.container = null
        }
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
            item.mobInventory = null
            item.mobEquipped = null
            item.room = null
            item.container = container
        }
    }

    fun decrementDecayTimer() {
        transaction {
            Items.update({ decayTimer.isNotNull() }) {
                with(SqlExpressionBuilder) {
                    it.update(decayTimer, decayTimer - 1)
                }
            }
            Items.deleteWhere(null as Int?, null as Int?) {
                decayTimer.isNotNull() and (decayTimer less 0)
            }
        }
    }

    fun createCorpseFromMob(mob: MobDAO): ItemDAO {
        val item = transaction {
            Items.update({ Items.mobEquippedId eq mob.id }) {
                it[mobEquippedId] = null
            }
            ItemDAO.new {
                name = "a corpse of $mob"
                description = "a corpse of $mob is here."
                level = mob.level
                weight = 100.0
                decayTimer = 20
                attributes = AttributesDAO.new {}
            }
        }
        transferAllItemsToItem(mob, item)
        return item
    }

    private fun transferAllItemsToItem(from: HasInventory, to: HasInventory) {
        transaction {
            Items.update({ getColumn(from) eq from.id }) {
                it[mobEquippedId] = null
                it[mobInventoryId] = null
                it[roomId] = null
                it[itemId] = to.id
            }
        }
    }
}
