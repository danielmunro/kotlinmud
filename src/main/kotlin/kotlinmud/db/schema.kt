package kotlinmud.db

import kotlinmud.db.enum.DirectionTable
import kotlinmud.db.enum.DispositionTable
import kotlinmud.exit.Exits
import kotlinmud.item.Items
import kotlinmud.mob.Mobs
import kotlinmud.room.Rooms
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun applyDBSchema() {
    println("applying DB schema")
    transaction {
        addLogger(StdOutSqlLogger)
//        exec("CREATE TYPE DispositionEnum AS ENUM ('dead', 'sitting', 'standing', 'fighting')")
//        exec("CREATE TYPE DirectionEnum AS ENUM ('north', 'south', 'east', 'west', 'up', 'down')")
        SchemaUtils.create(
            DispositionTable,
            DirectionTable,
            Mobs,
            Rooms,
            Exits,
            Items)
    }
}
