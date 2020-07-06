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
    companion object : IntEntityClass<RoomDAO>(Rooms) {
        private fun isDoorPassable(door: DoorDAO?): Boolean {
            return door == null || door.disposition == DoorDisposition.OPEN
        }
    }

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

    fun getExitsWithDoors(): Map<Direction, RoomDAO> {
        val exits = mutableMapOf<Direction, RoomDAO>()
        if (isDoorPassable(northDoor)) {
            exits.plus(Pair(Direction.NORTH, north))
        }
        if (isDoorPassable(southDoor)) {
            exits.plus(Pair(Direction.SOUTH, south))
        }
        if (isDoorPassable(eastDoor)) {
            exits.plus(Pair(Direction.EAST, east))
        }
        if (isDoorPassable(westDoor)) {
            exits.plus(Pair(Direction.WEST, west))
        }
        if (isDoorPassable(upDoor)) {
            exits.plus(Pair(Direction.UP, up))
        }
        if (isDoorPassable(downDoor)) {
            exits.plus(Pair(Direction.DOWN, down))
        }
        return exits
    }

    fun getAllExits(): Map<Direction, RoomDAO> {
        val exits = mutableMapOf<Direction, RoomDAO>()
        if (north != null) {
            exits.plus(Pair(Direction.NORTH, north))
        }
        if (south != null) {
            exits.plus(Pair(Direction.SOUTH, south))
        }
        if (east != null) {
            exits.plus(Pair(Direction.EAST, east))
        }
        if (west != null) {
            exits.plus(Pair(Direction.WEST, west))
        }
        if (up != null) {
            exits.plus(Pair(Direction.UP, up))
        }
        if (down != null) {
            exits.plus(Pair(Direction.DOWN, down))
        }
        return exits
    }

    fun isDoorPassable(direction: Direction): Boolean {
        return when (direction) {
            Direction.NORTH -> isDoorPassable(northDoor)
            Direction.SOUTH -> isDoorPassable(southDoor)
            Direction.EAST -> isDoorPassable(eastDoor)
            Direction.WEST -> isDoorPassable(westDoor)
            Direction.UP -> isDoorPassable(upDoor)
            Direction.DOWN -> isDoorPassable(downDoor)
        }
    }

    fun isElevationPassable(direction: Direction): Boolean {
        return when (direction) {
            Direction.NORTH -> isElevationPassable(north)
            Direction.SOUTH -> isElevationPassable(south)
            Direction.EAST -> isElevationPassable(east)
            Direction.WEST -> isElevationPassable(west)
            Direction.UP -> isElevationPassable(up)
            Direction.DOWN -> isElevationPassable(down)
        }
    }

    private fun isExitAvailable(door: DoorDAO?, exit: RoomDAO?): Boolean {
        return isDoorPassable(door) && exit != null && isElevationPassable(exit)
    }

    private fun isElevationPassable(room: RoomDAO?): Boolean {
        return room != null && room.elevation - elevation < MAX_WALKABLE_ELEVATION
    }
}
