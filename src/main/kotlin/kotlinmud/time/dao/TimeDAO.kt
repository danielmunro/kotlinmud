package kotlinmud.time.dao

import kotlinmud.time.table.Times
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class TimeDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TimeDAO>(Times)

    var time by Times.time
}
