package kotlinmud.room.table

import org.jetbrains.exposed.dao.IntIdTable

object Resources : IntIdTable() {
    val type = varchar("type", 255)
    val roomId = reference("room", Rooms)
}
