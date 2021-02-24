package kotlinmud.item.repository

import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.table.Items
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.mob.model.Mob
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

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

fun removeAllEquipmentForMob(mob: Mob) {
    mob.items.addAll(mob.equipped)
    mob.equipped.clear()
}

fun countItemsByCanonicalId(id: ItemCanonicalId): Int {
    return transaction {
        Items.select { Items.canonicalId eq id.toString() }.count()
    }
}
