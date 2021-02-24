package kotlinmud.item.helper

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.mob.model.Mob
import org.jetbrains.exposed.sql.transactions.transaction

fun applyAffectFromItem(mob: Mob, item: ItemDAO) {
    transaction {
        item.affects.forEach {
            val affect = AffectDAO.new {
                type = it.type
                timeout = it.timeout
            }
            mob.affects.add(affect)
        }
    }
}
