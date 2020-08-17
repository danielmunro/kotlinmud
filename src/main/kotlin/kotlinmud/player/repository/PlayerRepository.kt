package kotlinmud.player.repository

import kotlinmud.player.dao.PlayerDAO
import kotlinmud.player.table.Players
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun findPlayerByOTP(otp: String): PlayerDAO? {
    return transaction {
        Players.select { Players.lastOTP eq otp }.firstOrNull()?.let {
            PlayerDAO.wrapRow(it)
        }
    }
}

fun findPlayerByEmail(email: String): PlayerDAO? {
    return transaction {
        Players.select { Players.email eq email }.firstOrNull()?.let {
            PlayerDAO.wrapRow(it)
        }
    }
}
