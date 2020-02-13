package kotlinmud.exit

import kotlinmud.db.enum.DirectionPGEnum
import kotlinmud.room.Direction
import kotlinmud.room.RoomEntity
import kotlinmud.room.Rooms
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Exits : IntIdTable() {
    val room = reference("source", Rooms)
    val destination = reference("destination", Rooms)
    val direction = customEnumeration(
        "direction",
        "DirectionEnum",
        { direction -> Direction.values().first {
            it.toString().toLowerCase() == direction
        } },
        { DirectionPGEnum("DirectionEnum", it) })
}

class ExitEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ExitEntity>(Exits)

    var room by RoomEntity referencedOn Exits.room
    var destination by RoomEntity referencedOn Exits.destination
    var direction by Exits.direction
}
