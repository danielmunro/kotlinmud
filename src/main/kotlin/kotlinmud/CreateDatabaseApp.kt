package kotlinmud

import kotlinmud.affect.table.Affects
import kotlinmud.attributes.table.Attributes
import kotlinmud.db.createConnection
import kotlinmud.item.table.Items
import kotlinmud.mob.skill.table.Skills
import kotlinmud.mob.table.Mobs
import kotlinmud.room.table.Doors
import kotlinmud.room.table.Resources
import kotlinmud.room.table.Rooms
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    createConnection()
    transaction {
        SchemaUtils.create(
            Mobs,
            Skills,
            Affects,
            Items,
            Rooms,
            Doors,
            Attributes,
            Resources
        )
    }
    println("mobs: ${Mobs.selectAll()}")
}
