package kotlinmud.room.service

import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.model.Room

class RoomService {
    private val rooms = mutableListOf<Room>()
    private val areas = mutableSetOf<String>()

    fun builder(name: String, description: String, area: String): RoomBuilder {
        return RoomBuilder(this).also {
            it.name = name
            it.description = description
            it.area = area
        }
    }

    fun getRoomCount(): Int {
        return rooms.size
    }

    fun add(room: Room) {
        rooms.add(room)
        areas.add(room.area)
    }

    fun getAllAreas(): List<String> {
        return areas.toList()
    }

    fun findArea(partial: String): String? {
        return areas.find { it.startsWith(partial) }
    }

    fun findOne(predicate: (room: Room) -> Boolean): Room? {
        return rooms.find(predicate)
    }

    fun findByArea(area: String): List<Room> {
        return rooms.filter { it.area == area }
    }

    fun filter(predicate: (room: Room) -> Boolean): List<Room> {
        return rooms.filter(predicate)
    }

    fun getStartRoom(): Room {
        return rooms.find { it.id == 1 }!!
    }

    fun removeDecayedItems() {
        rooms.forEach {
            it.items.removeIf { item ->
                item.decayTimer != null && item.decayTimer!! <= 0
            }
        }
    }
}
