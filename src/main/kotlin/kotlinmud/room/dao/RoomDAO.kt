package kotlinmud.room.dao

import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.ResourceType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.table.Items
import kotlinmud.item.type.HasInventory
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.model.MAX_WALKABLE_ELEVATION
import kotlinmud.mob.table.Mobs
import kotlinmud.room.model.Door
import kotlinmud.room.table.Rooms
import kotlinmud.room.type.Direction
import kotlinmud.room.type.DoorDisposition
import kotlinmud.room.type.RegenLevel
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class RoomDAO(id: EntityID<Int>) : IntEntity(id), HasInventory {
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
    override val items by ItemDAO optionalReferrersOn Items.roomId
    var north by RoomDAO optionalReferencedOn Rooms.northId
    var northDoor by DoorDAO optionalReferencedOn Rooms.northDoorId
    var south by RoomDAO optionalReferencedOn Rooms.southId
    var southDoor by DoorDAO optionalReferencedOn Rooms.southDoorId
    var east by RoomDAO optionalReferencedOn Rooms.eastId
    var eastDoor by DoorDAO optionalReferencedOn Rooms.eastDoorId
    var west by RoomDAO optionalReferencedOn Rooms.westId
    var westDoor by DoorDAO optionalReferencedOn Rooms.westDoorId
    var up by RoomDAO optionalReferencedOn Rooms.upId
    var upDoor by DoorDAO optionalReferencedOn Rooms.upDoorId
    var down by RoomDAO optionalReferencedOn Rooms.downId
    var downDoor by DoorDAO optionalReferencedOn Rooms.downDoorId
    var owner by MobDAO optionalReferencedOn Rooms.ownerId

    fun getDoors(): Map<Direction, DoorDAO> {
        val exits = mutableMapOf<Direction, DoorDAO>()
        if (northDoor != null) {
            exits.plus(Pair(Direction.NORTH, northDoor))
        }
        if (southDoor != null) {
            exits.plus(Pair(Direction.SOUTH, southDoor))
        }
        if (eastDoor != null) {
            exits.plus(Pair(Direction.EAST, eastDoor))
        }
        if (westDoor != null) {
            exits.plus(Pair(Direction.WEST, westDoor))
        }
        if (upDoor != null) {
            exits.plus(Pair(Direction.UP, upDoor))
        }
        if (downDoor != null) {
            exits.plus(Pair(Direction.DOWN, downDoor))
        }
        return exits
    }

    fun getAvailableExits(): Map<Direction, RoomDAO> {
        val exits = mutableMapOf<Direction, RoomDAO>()
        if (isExitAvailable(northDoor, north)) {
            exits.plus(Pair(Direction.NORTH, north))
        }
        if (isExitAvailable(southDoor, south)) {
            exits.plus(Pair(Direction.SOUTH, south))
        }
        if (isExitAvailable(eastDoor, east)) {
            exits.plus(Pair(Direction.EAST, east))
        }
        if (isExitAvailable(westDoor, west)) {
            exits.plus(Pair(Direction.WEST, west))
        }
        if (isExitAvailable(upDoor, up)) {
            exits.plus(Pair(Direction.UP, up))
        }
        if (isExitAvailable(downDoor, down)) {
            exits.plus(Pair(Direction.DOWN, down))
        }
        return exits
    }

    private fun isExitAvailable(door: DoorDAO?, exit: RoomDAO?): Boolean {
        return door == null || door.disposition == DoorDisposition.OPEN
                && exit != null && exit.elevation - elevation < MAX_WALKABLE_ELEVATION
    }
}
