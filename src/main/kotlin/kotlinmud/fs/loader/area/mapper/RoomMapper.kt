package kotlinmud.fs.loader.area.mapper

import kotlinmud.fs.loader.area.model.RoomModel
import kotlinmud.mob.model.Mob
import kotlinmud.room.model.Door
import kotlinmud.room.model.Exit
import kotlinmud.room.model.Room
import kotlinmud.room.model.RoomBuilder
import kotlinmud.room.type.Direction
import kotlinmud.room.type.oppositeDirection

class RoomMapper(val mobs: List<Mob>, val roomModels: List<RoomModel>, val doors: List<Door>) {
    fun map(): List<Room> {
        val rooms = roomModels.map {
            val mob = if (it.ownerId > 0) mobs.find { mob -> it.ownerId == mob.id } else null
            RoomBuilder()
                .id(it.id)
                .area(it.area)
                .name(it.name)
                .description(it.description)
                .regen(it.regen)
                .isIndoor(it.isIndoor)
                .biome(it.biomeType)
                .resources(it.resources.toMutableList())
                .owner(mob)
                .build()
        }
        val modelMap = mutableMapOf<Int, RoomModel>()
        val roomMap = mutableMapOf<Int, Room>()
        roomModels.forEach {
            modelMap += Pair(it.id, it)
        }
        rooms.forEach {
            roomMap += Pair(it.id, it)
        }
        rooms.forEach { room ->
            addExit(room, roomMap, Direction.NORTH, modelMap[room.id]?.north)
            addExit(room, roomMap, Direction.SOUTH, modelMap[room.id]?.south)
            addExit(room, roomMap, Direction.EAST, modelMap[room.id]?.east)
            addExit(room, roomMap, Direction.WEST, modelMap[room.id]?.west)
            addExit(room, roomMap, Direction.UP, modelMap[room.id]?.up)
            addExit(room, roomMap, Direction.DOWN, modelMap[room.id]?.down)
        }
        return rooms
    }

    private fun addExit(room: Room, roomMap: Map<Int, Room>, direction: Direction, id: String?, door: Door? = null) {
        if (id == null || id == "") {
            return
        }
        room.exits.find { it.direction == direction }?.let {
            return
        }
        if (id.contains("-")) {
            val parts = id.split("-")
            addExit(room, roomMap, direction, parts[2], doors.find {
                it.id == parts[1].toInt()
            })
            return
        }
        val toInt = id.toInt()
        if (toInt == 0) {
            return
        }
        val destination = roomMap[toInt] ?: error("no map for $id")
        room.exits.add(Exit(destination, direction, door))
        destination.exits.add(Exit(room, oppositeDirection(direction), door))
    }
}
