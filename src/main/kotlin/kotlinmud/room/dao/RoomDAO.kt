package kotlinmud.room.dao

import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.ResourceType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.table.Mobs
import kotlinmud.room.table.Rooms
import kotlinmud.room.type.RegenLevel
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class RoomDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoomDAO>(Rooms)

    var name by Rooms.name
    var area by Rooms.area
    var description by Rooms.description
    var isIndoor by Rooms.isIndoor
    var regenLevel: RegenLevel by Rooms.regenLevel.transform({ it.toString() }, { RegenLevel.valueOf(it) })
    var biome: BiomeType by Rooms.biome.transform({ it.toString() }, { BiomeType.valueOf(it) })
    var substrate: SubstrateType by Rooms.substrate.transform({ it.toString() }, { SubstrateType.valueOf(it) })
    var resources: MutableList<ResourceType> by Rooms.resources.transform(
        {
            it.joinToString(", ")
        },
        {
            it.split(", ").map { ResourceType.valueOf(it) }.toMutableList()
        }
    )
    var elevation by Rooms.elevation
    var north by RoomDAO referencedOn Rooms.northId
    var northDoor by DoorDAO referencedOn Rooms.northDoorId
    var south by RoomDAO referencedOn Rooms.southId
    var southDoor by DoorDAO referencedOn Rooms.southDoorId
    var east by RoomDAO referencedOn Rooms.eastId
    var eastDoor by DoorDAO referencedOn Rooms.eastDoorId
    var west by RoomDAO referencedOn Rooms.westId
    var westDoor by DoorDAO referencedOn Rooms.westDoorId
    var up by RoomDAO referencedOn Rooms.upId
    var upDoor by DoorDAO referencedOn Rooms.upDoorId
    var down by RoomDAO referencedOn Rooms.downId
    var downDoor by DoorDAO referencedOn Rooms.downDoorId
    var owner by MobDAO referencedOn Rooms.ownerId
}
