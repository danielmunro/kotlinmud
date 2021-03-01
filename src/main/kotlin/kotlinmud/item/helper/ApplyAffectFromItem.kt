package kotlinmud.item.helper

import kotlinmud.affect.model.Affect
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item
import kotlinmud.mob.model.Mob
import org.jetbrains.exposed.sql.transactions.transaction

fun applyAffectFromItem(mob: Mob, item: Item) {
    transaction {
        item.affects.forEach {
            mob.affects.add(Affect(it.type, it.timeout, it.attributes))
        }
    }
}
