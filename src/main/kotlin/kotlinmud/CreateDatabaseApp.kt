package kotlinmud

import java.sql.Connection
import kotlinmud.affect.table.Affects
import kotlinmud.attributes.table.Attributes
import kotlinmud.item.table.Items
import kotlinmud.mob.skill.table.Skills
import kotlinmud.mob.table.Mobs
import kotlinmud.room.table.Doors
import kotlinmud.room.table.Rooms
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    Database.connect("jdbc:sqlite:data.db", "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    transaction {
        SchemaUtils.create(
            Mobs,
            Skills,
            Affects,
            Items,
            Rooms,
            Doors,
            Attributes
        )
    }
    println("mobs: ${Mobs.selectAll()}")
}
