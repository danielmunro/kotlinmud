package kotlinmud.room

import kotlinmud.exit.Exit
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Room(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<Room>(Rooms)

    var name by Rooms.name
    var description by Rooms.description
    val exits by Exit referrersOn Rooms.exits
}