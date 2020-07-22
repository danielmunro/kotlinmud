package kotlinmud.player.table

import org.jetbrains.exposed.dao.IntIdTable

object Players : IntIdTable() {
    val name = varchar("name", 50)
    val email = varchar("email", 50)
    val created = datetime("created")
    val lastOTP = varchar("lastOTP", 255)
}
