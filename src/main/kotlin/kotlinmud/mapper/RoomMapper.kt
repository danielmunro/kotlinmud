package kotlinmud.mapper

import kotlinmud.loader.model.RoomModel
import kotlinmud.room.Direction
import kotlinmud.room.Room
import kotlinmud.room.exit.Exit
import kotlinmud.room.oppositeDirection

class RoomMapper(private val roomModels: List<RoomModel>) {
    fun map(): List<Room> {
        val rooms = roomModels.map { Room(it.id, it.name, it.description) }
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
}

fun addExit(room: Room, roomMap: Map<Int, Room>, direction: Direction, id: Int?) {
    if (id != null && id != 0) {
        val destination = roomMap[id] ?: error("no map for $id")
        room.exits.add(Exit(destination, direction))
        destination.exits.add(Exit(room, oppositeDirection(direction)))
    }
}
