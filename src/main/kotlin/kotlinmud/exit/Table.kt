package kotlinmud.exit

import kotlinmud.room.Rooms
import org.jetbrains.exposed.dao.IntIdTable

object Exits: IntIdTable() {
    val room = reference("source", Rooms)
    val destination = reference("destination", Rooms)
    var direction = varchar("direction", 50)
}
