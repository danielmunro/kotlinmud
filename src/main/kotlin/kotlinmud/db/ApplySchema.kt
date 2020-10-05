package kotlinmud.db

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun applySchema() {
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(
            *getTables()
        )
    }
}
