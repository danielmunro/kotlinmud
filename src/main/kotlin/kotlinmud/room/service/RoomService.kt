package kotlinmud.room.service

import kotlinmud.persistence.model.RoomModel
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.factory.createInitialAreas
import kotlinmud.room.model.Area
import kotlinmud.room.model.Room
import kotlinmud.service.BaseService

class RoomService : BaseService() {
    private val rooms = mutableListOf<Room>()
    private val areas = mutableListOf<Area>()

    init {
        areas.addAll(createInitialAreas())
    }

    fun builder(name: String, description: String, area: Area): RoomBuilder {
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
    }

    fun addArea(area: Area) {
        areas.add(area)
    }

    fun getAllAreas(): List<Area> {
        return areas
    }

    fun findArea(partial: String): Area? {
        return areas.find { it.name.startsWith(partial) }
    }

    fun findOne(predicate: (room: Room) -> Boolean): Room? {
        return rooms.find(predicate)
    }

    fun findByArea(area: Area): List<Room> {
        return rooms.filter { it.area.name == area.name }
    }

    fun findRoomModels(area: Area): List<RoomModel> {
        return models.filterIsInstance<RoomModel>().filter { it.area == area }
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
