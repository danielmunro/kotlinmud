package kotlinmud.time.repository

import kotlinmud.time.dao.TimeDAO
import kotlinmud.time.table.Times
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun findTime(): TimeDAO {
    return transaction {
        Times.selectAll().firstOrNull()?.let {
            TimeDAO.wrapRow(it)
        } ?: TimeDAO.new {
            time = 0
        }
    }
}
