package kotlinmud.db

import kotlinmud.exit.Exits
import kotlinmud.item.Items
import kotlinmud.mob.Mobs
import kotlinmud.room.Rooms
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun applyDBSchema() {
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(
            Mobs,
            Rooms,
            Exits,
            Items)
    }
}