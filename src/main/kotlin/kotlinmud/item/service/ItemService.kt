package kotlinmud.item.service

import kotlinmud.affect.type.AffectType
import kotlinmud.helper.string.matches
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item
import kotlinmud.item.model.ItemBuilder
import kotlinmud.item.model.ItemOwner
import kotlinmud.item.table.Items
import kotlinmud.item.table.Items.decayTimer
import kotlinmud.item.table.Items.mobInventoryId
import kotlinmud.item.table.Items.roomId
import kotlinmud.item.type.HasInventory
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.model.corpseWeight
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.*

typealias ItemBuilderBuilder = () -> ItemBuilder

class ItemService {
    fun countItemsByCanonicalId(id: UUID): Int {
        return transaction {
            Items.select { Items.canonicalId eq id }.count()
        }
    }

    fun findByOwner(mob: MobDAO, input: String): ItemDAO? {
        return transaction {
            ItemDAO.wrapRows(
                Items.select( mobInventoryId eq mob.id and (Items.name like "$input%") )
            ).firstOrNull()
        }
    }

    fun findByRoom(room: RoomDAO, input: String): ItemDAO? {
        return transaction {
            ItemDAO.wrapRows(
                Items.select( roomId eq room.id and (Items.name like "$input%") )
            ).firstOrNull()
        }
    }

    fun findAllByOwner(hasInventory: HasInventory): List<ItemDAO> {
        return transaction {
            ItemDAO.wrapRows(
                Items.select {
                    mobInventoryId eq hasInventory.id
                }
            ).toList()
        }
    }

    fun getItemGroups(mob: MobDAO): Map<EntityID<Int>, List<ItemDAO>> {
        return findAllByOwner(mob).groupBy { it.id }
    }

    fun giveItemToMob(item: ItemDAO, mob: MobDAO) {
        transaction {
            Items.update({ Items.id eq item.id }) {
                it[mobInventoryId] = mob.id
            }
        }
    }

    fun decrementDecayTimer() {
        transaction {
            Items.update({ decayTimer.isNotNull() }) {
                decayTimer less 1
            }
            Items.deleteWhere(9999 as Int, 0 as Int) {
                decayTimer.isNotNull() and (decayTimer less 0)
            }
        }
    }

    fun destroy(item: ItemDAO) {
        transaction {
            item.delete()
        }
    }

    fun transferAllItems(from: IntEntity, to: IntEntity) {
        transaction {
            Items.update({ mobInventoryId eq from.id }) {
                it[mobInventoryId] = to.id
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
                weight = corpseWeight
                decayTimer = 20
            }
        }
        transferAllItems(mob, item)
        return item
    }
}
