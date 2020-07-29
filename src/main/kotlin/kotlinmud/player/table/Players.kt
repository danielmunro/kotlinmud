package kotlinmud.player.table

import org.jetbrains.exposed.dao.IntIdTable
import org.joda.time.DateTime

object Players : IntIdTable() {
    val name = varchar("name", 50)
    val email = varchar("email", 50)
    val created = datetime("created").default(DateTime.now())
    val lastOTP = varchar("lastOTP", 255).nullable()
}
