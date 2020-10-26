package kotlinmud.room.dao

import kotlinmud.biome.type.ResourceType
import kotlinmud.room.table.Resources
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class ResourceDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ResourceDAO>(Resources)

    var type by Resources.type.transform(
        { it.toString() },
        { ResourceType.valueOf(it) }
    )
    var name by Resources.name
    var maturity by Resources.maturity
    var maturesAt by Resources.maturesAt
    var room by RoomDAO referencedOn Resources.roomId
}
