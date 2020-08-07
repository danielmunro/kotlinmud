package kotlinmud.affect.repository

import kotlinmud.affect.table.Affects
import org.jetbrains.exposed.sql.SqlExpressionBuilder.minus
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun deleteTimedOutAffects() {
    transaction {
        // see: https://stackoverflow.com/questions/38779666/how-to-fix-overload-resolution-ambiguity-in-kotlin-no-lambda
        Affects.deleteWhere(null as Int?, null as Int?) {
            Affects.timeout.isNotNull() and (Affects.timeout eq 0)
        }
    }
}

fun decrementAffectsTimeout() {
    transaction {
        Affects.update({ Affects.timeout.isNotNull() }) {
            it.update(timeout, timeout - 1)
        }
    }
}
