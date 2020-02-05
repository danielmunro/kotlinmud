package kotlinmud.room

import kotlinmud.exit.Exit
import kotlinmud.exit.Exits
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

class Room(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<Room>(
        Rooms
    )

    var name by Rooms.name
    var description by Rooms.description
    val exits by Exit referrersOn Rooms.exits
}

object Rooms: IntIdTable() {
    val name = varchar("name", 50)
    val description = text("description")
    val exits = reference("exits", Exits)
}