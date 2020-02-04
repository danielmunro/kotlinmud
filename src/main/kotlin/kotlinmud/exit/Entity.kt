package kotlinmud.exit

import kotlinmud.room.Room
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Exit(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<Exit>(Exits)

    var room by Room referencedOn Exits.room
    var destination by Room referencedOn Exits.destination
    var direction by Exits.direction
}
