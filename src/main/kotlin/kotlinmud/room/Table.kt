package kotlinmud.room

import kotlinmud.exit.Exits
import org.jetbrains.exposed.dao.IntIdTable

object Rooms: IntIdTable() {
    val name = varchar("name", 50)
    val description = text("description")
    val exits = reference("exits", Exits)
}
