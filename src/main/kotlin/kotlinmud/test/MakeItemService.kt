package kotlinmud.test

import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.mob.dao.MobDAO

class MakeItemService(private val amount: Int) {
    var item: ItemDAO? = null

    fun lumber(): MakeItemService {
        item = ItemDAO.new {
            name = "lumber"
            description = "Fine pine lumber is here."
            type = ItemType.LUMBER
        }
        return this
    }

    fun andGiveTo(mob: MobDAO) {
        for (i in 1..amount) {
            val copy = ItemDAO.new(item!!.id.value) {}
            copy.mobInventory = mob
        }
    }
}
