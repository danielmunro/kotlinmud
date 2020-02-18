package kotlinmud.exit

import kotlinmud.db.enum.DirectionPGEnum
import kotlinmud.room.Direction
import kotlinmud.room.RoomEntity
import kotlinmud.room.RoomTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object ExitTable : IntIdTable() {
    val room = reference("source", RoomTable)
    val destination = reference("destination", RoomTable)
    val direction = customEnumeration(
        "direction",
        "DirectionEnum",
        { direction -> Direction.values().first {
            it.toString().toLowerCase() == direction
        } },
        { DirectionPGEnum("DirectionEnum", it) })
}

class ExitEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ExitEntity>(ExitTable)

    var room by RoomEntity referencedOn ExitTable.room
    var destination by RoomEntity referencedOn ExitTable.destination
    var direction by ExitTable.direction
}
