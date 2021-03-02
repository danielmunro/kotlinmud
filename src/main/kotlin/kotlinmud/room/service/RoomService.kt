package kotlinmud.room.service

import kotlinmud.room.model.Room
import kotlinmud.room.type.Area

class RoomService {
    private val rooms = mutableListOf<Room>()

    fun add(room: Room) {
        rooms.add(room)
    }

    fun find(predicate: (room: Room) -> Boolean): Room? {
        return rooms.find(predicate)
    }

    fun findByArea(area: Area): List<Room> {
        return rooms.filter { it.area == area }
    }

    fun getStartRoom(): Room {
        return rooms.first()
    }
}
