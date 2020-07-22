package kotlinmud.db

import kotlinmud.affect.table.Affects
import kotlinmud.attributes.table.Attributes
import kotlinmud.item.table.Items
import kotlinmud.mob.skill.table.Skills
import kotlinmud.mob.table.Mobs
import kotlinmud.player.table.MobCards
import kotlinmud.player.table.Players
import kotlinmud.room.table.Doors
import kotlinmud.room.table.Rooms
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun applySchema() {
    transaction {
        SchemaUtils.create(
            Mobs,
            Skills,
            Affects,
            Items,
            Rooms,
            Doors,
            Attributes,
            Players,
            MobCards
        )
    }
}
