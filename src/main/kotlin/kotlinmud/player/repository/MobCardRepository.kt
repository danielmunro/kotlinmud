package kotlinmud.player.repository

import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.table.MobCards
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
