package kotlinmud.mob

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Mob(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<Mob>(Mobs)

    var name by Mobs.name
    var description by Mobs.description
}
