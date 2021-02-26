package kotlinmud.player.repository

import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.table.MobCards
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun updateAllMobCardsLoggedOut() {
    transaction {
        MobCards.update({ MobCards.loggedIn eq true }) {
            it[loggedIn] = false
        }
    }
}

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
        MobCards.select {
            MobCards.mobName eq name
        }.firstOrNull()?.let {
            MobCardDAO.wrapRow(it)
        }
    }
}
