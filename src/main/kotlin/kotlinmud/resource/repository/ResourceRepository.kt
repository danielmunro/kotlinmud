package kotlinmud.resource.repository

import kotlinmud.room.table.Resources
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.update

fun incrementResourceMaturity() {
    Resources.update({ Resources.maturity neq null and (Resources.maturity less Resources.maturesAt) }) {
        with(SqlExpressionBuilder) {
            it.update(maturity, maturity + 1)
        }
    }
}
