package kotlinmud.room.service

import kotlinmud.biome.type.BiomeType
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.model.Room
import kotlinmud.room.type.Area

class RoomService {
    private val rooms = mutableListOf<Room>()

    fun builder(name: String, description: String, area: Area): RoomBuilder {
        return RoomBuilder(this).also {
            it.name = name
            it.description = description
            it.area = area
        }
    }

    fun add(room: Room) {
        rooms.add(room)
    }

    fun findOne(predicate: (room: Room) -> Boolean): Room? {
        return rooms.find(predicate)
    }

    fun findByArea(area: Area): List<Room> {
        return rooms.filter { it.area == area }
    }

    fun findByBiome(biomeType: BiomeType): List<Room> {
        return rooms.filter { it.biome == biomeType }
    }

    fun filter(predicate: (room: Room) -> Boolean): List<Room> {
        return rooms.filter(predicate)
    }

    fun getStartRoom(): Room {
        return rooms.first()
    }

    fun removeDecayedItems() {
        rooms.forEach {
            it.items.removeIf { item ->
                item.decayTimer != null && item.decayTimer!! <= 0
            }
        }
    }
}
