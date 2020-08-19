package kotlinmud.player.repository

import kotlinmud.mob.table.Mobs
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.table.MobCards
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun findLoggedInMobCards(): List<MobCardDAO> {
    return transaction {
        MobCardDAO.wrapRows(
            MobCards.select {
                MobCards.loggedIn eq true
            }
        ).toList()
    }
}

fun findMobCardByName(name: String): MobCardDAO? {
    return transaction {
        (Mobs innerJoin MobCards).select {
            Mobs.mobCardId eq MobCards.id and (Mobs.name eq name)
        }.firstOrNull()?.let {
            MobCardDAO.wrapRow(it)
        }
    }
}
