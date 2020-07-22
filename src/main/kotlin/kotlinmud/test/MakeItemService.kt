package kotlinmud.test

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.mob.dao.MobDAO
import org.jetbrains.exposed.sql.transactions.transaction

class MakeItemService(private val amount: Int) {
    var item: ItemDAO? = null

    fun lumber(): MakeItemService {
        return this
    }

    fun andGiveTo(mob: MobDAO) {
        transaction {
            for (i in 1..amount) {
                createItem().mobInventory = mob
            }
        }
    }

    private fun createItem(): ItemDAO {
        return ItemDAO.new {
            name = "lumber"
            description = "Fine pine lumber is here."
            type = ItemType.LUMBER
            attributes = AttributesDAO.new {}
        }
    }
}
