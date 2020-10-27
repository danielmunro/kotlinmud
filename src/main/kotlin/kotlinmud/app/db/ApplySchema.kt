package kotlinmud.app.db

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun applySchema() {
    transaction {
        SchemaUtils.create(
            *getTables()
        )
    }
}
