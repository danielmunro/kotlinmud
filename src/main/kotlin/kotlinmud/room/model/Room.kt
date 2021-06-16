package kotlinmud.room.model

import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.ResourceType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.helper.Noun
import kotlinmud.item.model.Item
import kotlinmud.item.type.HasInventory
import kotlinmud.mob.constant.MAX_WALKABLE_ELEVATION
import kotlinmud.mob.model.Mob
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.room.type.DoorDisposition
import kotlinmud.room.type.RegenLevel
import kotlinmud.type.RoomCanonicalId

class Room(
    val id: Int,
    val label: String?,
    override val name: String,
    override val brief: String,
    override val description: String,
    val canonicalId: RoomCanonicalId?,
    val area: Area,
    val isIndoors: Boolean,
    val regenLevel: RegenLevel,
    val biome: BiomeType,
    var substrateType: SubstrateType,
    val elevation: Int,
    val maxWeight: Int,
    val maxItems: Int,
    override val items: MutableList<Item>,
    val resources: MutableList<ResourceType>,
    val owner: Mob?
) : HasInventory, Noun {
    var north: Room? = null
    var northDoor: Door? = null
    var south: Room? = null
    var southDoor: Door? = null
    var east: Room? = null
    var eastDoor: Door? = null
    var west: Room? = null
    var westDoor: Door? = null
    var up: Room? = null
    var upDoor: Door? = null
    var down: Room? = null
    var downDoor: Door? = null

    fun getAllExits(): Map<Direction, Room> {
        val exits = mutableMapOf<Direction, Room>()
        north?.let { exits[Direction.NORTH] = it }
        south?.let { exits[Direction.SOUTH] = it }
        east?.let { exits[Direction.EAST] = it }
        west?.let { exits[Direction.WEST] = it }
        up?.let { exits[Direction.UP] = it }
        down?.let { exits[Direction.DOWN] = it }
        return exits
    }

    fun getDoors(): Map<Direction, Door> {
        val exits = mutableMapOf<Direction, Door>()
        northDoor?.let { exits[Direction.NORTH] = it }
        southDoor?.let { exits[Direction.SOUTH] = it }
        eastDoor?.let { exits[Direction.EAST] = it }
        westDoor?.let { exits[Direction.WEST] = it }
        upDoor?.let { exits[Direction.UP] = it }
        downDoor?.let { exits[Direction.DOWN] = it }
        return exits
    }

    fun getDoorForDirection(direction: Direction): Door? {
        return when (direction) {
            Direction.NORTH -> northDoor
            Direction.SOUTH -> southDoor
            Direction.EAST -> eastDoor
            Direction.WEST -> westDoor
            Direction.UP -> upDoor
            Direction.DOWN -> downDoor
        }
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

    private fun isElevationPassable(room: Room?): Boolean {
        return room != null && room.elevation - elevation < MAX_WALKABLE_ELEVATION
    }

    private fun isDoorPassable(door: Door?): Boolean {
        return door == null || door.disposition == DoorDisposition.OPEN
    }
}
