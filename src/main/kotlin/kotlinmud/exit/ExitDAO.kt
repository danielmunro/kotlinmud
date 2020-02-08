package kotlinmud.exit

import kotlinmud.room.Direction
import kotlinmud.room.Room
import kotlinmud.room.Rooms
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Exits: IntIdTable() {
    val room = reference("source", Rooms)
    val destination = reference("destination", Rooms)
    val direction = customEnumeration(
        "direction",
        "ENUM('north', 'south', 'east', 'west', 'up', 'down')",
        { direction -> Direction.values().first {
            it.toString().toLowerCase() == direction
        } },
        { it.name })
}

class Exit(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<Exit>(Exits)

    var room by Room referencedOn Exits.room
    var destination by Room referencedOn Exits.destination
    var direction by Exits.direction
}
