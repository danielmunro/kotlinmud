package kotlinmud.time.table

import org.jetbrains.exposed.dao.IntIdTable

object Times : IntIdTable() {
    val time = integer("time")
}
