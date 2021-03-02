package kotlinmud.room.dao

import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.table.Items
import kotlinmud.mob.constant.MAX_WALKABLE_ELEVATION
import kotlinmud.mob.dao.MobDAO
import kotlinmud.room.table.Resources
import kotlinmud.room.table.Rooms
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.room.type.DoorDisposition
import kotlinmud.room.type.RegenLevel
import kotlinmud.type.RoomCanonicalId
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.transactions.transaction

class RoomDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoomDAO>(Rooms) {
        private fun isDoorPassable(door: DoorDAO?): Boolean {
            return door == null || door.disposition == DoorDisposition.OPEN
        }
    }

    var name by Rooms.name
    var area by Rooms.area.transform(
        { it.toString() },
        { Area.valueOf(it) }
    )

    var description by Rooms.description
    var isIndoor by Rooms.isIndoor
    var regenLevel: RegenLevel by Rooms.regenLevel.transform({ it.toString() }, { RegenLevel.valueOf(it) })
    var biome: BiomeType by Rooms.biome.transform({ it.toString() }, { BiomeType.valueOf(it) })
    var substrate: SubstrateType by Rooms.substrate.transform({ it.toString() }, { SubstrateType.valueOf(it) })
    var canonicalId by Rooms.canonicalId.transform(
        { it.toString() },
        { it?.let { RoomCanonicalId.valueOf(it) } }
    )
    var elevation by Rooms.elevation
    var maxWeight by Rooms.maxWeight
    var maxItems by Rooms.maxItems
    val resources by ResourceDAO referrersOn Resources.roomId
    val items by ItemDAO optionalReferrersOn Items.roomId
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
        transaction {
            northDoor?.let { exits[Direction.NORTH] = it }
            southDoor?.let { exits[Direction.SOUTH] = it }
            eastDoor?.let { exits[Direction.EAST] = it }
            westDoor?.let { exits[Direction.WEST] = it }
            upDoor?.let { exits[Direction.UP] = it }
            downDoor?.let { exits[Direction.DOWN] = it }
        }
        return exits
    }

    fun getAllExits(): Map<Direction, RoomDAO> {
        val exits = mutableMapOf<Direction, RoomDAO>()
        transaction {
            north?.let { exits[Direction.NORTH] = it }
            south?.let { exits[Direction.SOUTH] = it }
            east?.let { exits[Direction.EAST] = it }
            west?.let { exits[Direction.WEST] = it }
            up?.let { exits[Direction.UP] = it }
            down?.let { exits[Direction.DOWN] = it }
        }
        return exits
    }

    fun isDoorPassable(direction: Direction): Boolean {
        return transaction {
            when (direction) {
                Direction.NORTH -> isDoorPassable(northDoor)
                Direction.SOUTH -> isDoorPassable(southDoor)
                Direction.EAST -> isDoorPassable(eastDoor)
                Direction.WEST -> isDoorPassable(westDoor)
                Direction.UP -> isDoorPassable(upDoor)
                Direction.DOWN -> isDoorPassable(downDoor)
            }
        }
    }

    fun getDoorForDirection(direction: Direction): DoorDAO? {
        return when (direction) {
            Direction.NORTH -> northDoor
            Direction.SOUTH -> southDoor
            Direction.EAST -> eastDoor
            Direction.WEST -> westDoor
            Direction.UP -> upDoor
            Direction.DOWN -> downDoor
        }
    }

    fun isElevationPassable(direction: Direction): Boolean {
        return transaction {
            when (direction) {
                Direction.NORTH -> isElevationPassable(north)
                Direction.SOUTH -> isElevationPassable(south)
                Direction.EAST -> isElevationPassable(east)
                Direction.WEST -> isElevationPassable(west)
                Direction.UP -> isElevationPassable(up)
                Direction.DOWN -> isElevationPassable(down)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is RoomDAO) other.id.value == id.value else super.equals(other)
    }

    private fun isElevationPassable(room: RoomDAO?): Boolean {
        return room != null && room.elevation - elevation < MAX_WALKABLE_ELEVATION
    }
}
