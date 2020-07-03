package kotlinmud.mob.provider

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.table.Mobs
import kotlinmud.mob.table.Mobs.isNpc
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun loadMobs(): MutableList<MobDAO> {
    return transaction {
        MobDAO.wrapRows(
            Mobs.select {
                isNpc.eq(false)
            }
        ).toMutableList()
    }
}
