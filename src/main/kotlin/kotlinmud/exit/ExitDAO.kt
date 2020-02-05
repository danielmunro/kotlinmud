package kotlinmud.exit

import kotlinmud.room.Room
import kotlinmud.room.Rooms
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

class Exit(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<Exit>(Exits)

    var room by Room referencedOn Exits.room
    var destination by Room referencedOn Exits.destination
    var direction by Exits.direction
}

object Exits: IntIdTable() {
    val room = reference("source", Rooms)
    val destination = reference("destination", Rooms)
    var direction = varchar("direction", 50)
}