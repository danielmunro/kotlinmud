package kotlinmud.room.dao

import kotlinmud.room.table.Doors
import kotlinmud.room.type.DoorDisposition
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class DoorDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DoorDAO>(Doors)

    var name by Doors.name
    var description by Doors.description
    var disposition: DoorDisposition by Doors.disposition.transform(
        { it.toString() },
        { DoorDisposition.valueOf(it) }
    )
    var defaultDisposition: DoorDisposition by Doors.defaultDisposition.transform(
        { it.toString() },
        { DoorDisposition.valueOf(it) }
    )

    override fun toString(): String {
        return name
    }
}
