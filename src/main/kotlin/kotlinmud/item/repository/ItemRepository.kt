package kotlinmud.item.repository

import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.service.ItemService
import kotlinmud.item.table.Items
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.mob.dao.MobDAO
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.SqlExpressionBuilder.minus
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun findAllItemsByOwner(hasInventory: HasInventory): List<ItemDAO> {
    return transaction {
        ItemDAO.wrapRows(
            Items.select {
                ItemService.getColumn(hasInventory) eq hasInventory.id
            }
        ).toList()
    }
}

fun findOneByMob(mob: MobDAO, input: String): ItemDAO? {
    return transaction {
        ItemDAO.wrapRows(
            Items.select(Items.mobInventoryId eq mob.id and (Items.name like "%$input%"))
        ).firstOrNull()
    }
}

fun findOneByRoom(room: RoomDAO, input: String): ItemDAO? {
    return transaction {
        ItemDAO.wrapRows(
            Items.select(Items.roomId eq room.id and (Items.name like "%$input%"))
        ).firstOrNull()
    }
}

fun decrementAllItemDecayTimers() {
    transaction {
        Items.update({ Items.decayTimer.isNotNull() }) {
            with(SqlExpressionBuilder) {
                it.update(decayTimer, decayTimer - 1)
            }
        }
        Items.deleteWhere(null as Int?, null as Int?) {
            Items.decayTimer.isNotNull() and (Items.decayTimer less 0)
        }
    }
}

fun removeAllEquipmentForMob(mob: MobDAO) {
    transaction {
        Items.update({ Items.mobEquippedId eq mob.id }) {
            it[mobEquippedId] = null
        }
    }
}

fun transferAllItemsToItemContainer(from: HasInventory, to: ItemDAO) {
    transaction {
        Items.update({ ItemService.getColumn(from) eq from.id }) {
            it[mobEquippedId] = null
            it[mobInventoryId] = null
            it[roomId] = null
            it[itemId] = to.id
        }
    }
}

fun countItemsByCanonicalId(id: ItemCanonicalId): Int {
    return transaction {
        Items.select { Items.canonicalId eq id.toString() }.count()
    }
}
