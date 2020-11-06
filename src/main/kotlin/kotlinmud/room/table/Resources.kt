package kotlinmud.room.table

import org.jetbrains.exposed.dao.IntIdTable

object Resources : IntIdTable() {
    val type = varchar("type", 255)
    val name = varchar("name", 255)
    val maturity = integer("maturity").nullable()
    val maturesAt = integer("maturesAt").nullable()
    val isPlanted = bool("isPlanted").default(false)
    val roomId = reference("room", Rooms)
}
