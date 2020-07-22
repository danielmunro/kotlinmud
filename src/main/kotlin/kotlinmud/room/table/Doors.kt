package kotlinmud.room.table

import org.jetbrains.exposed.dao.IntIdTable

object Doors : IntIdTable() {
    val name = varchar("name", 255)
    val description = text("description")
    val disposition = varchar("disposition", 50)
    val defaultDisposition = varchar("defaultDisposition", 50)
//    val keyItemId = reference("keyItemId", Items).nullable()
}
