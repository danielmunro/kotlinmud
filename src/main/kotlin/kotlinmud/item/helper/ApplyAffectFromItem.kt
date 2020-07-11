package kotlinmud.item.helper

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.mob.dao.MobDAO
import org.jetbrains.exposed.sql.transactions.transaction

fun applyAffectFromItem(mob: MobDAO, item: ItemDAO) {
    transaction {
        item.affects.forEach {
            mob.affects.plus(AffectDAO.new {
                type = it.type
                timeout = item.level
            })
        }
    }
}
