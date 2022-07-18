package kotlinmud.room.service

import kotlinmud.persistence.model.RoomModel
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.factory.createInitialAreas
import kotlinmud.room.model.Area
import kotlinmud.room.model.Room
import kotlinmud.service.BaseService
import kotlinmud.utility.changeLine
import kotlinmud.utility.removeLine

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
        area.id = getAreaNextId()
        areas.add(area)
    }

    fun getAllAreas(): List<Area> {
        return areas
    }

    fun findArea(partial: String): Area? {
        return areas.find { it.name.startsWith(partial) }
    }

    fun getAreaById(id: Int): Area {
        return areas.find { it.id == id }!!
    }

    fun findOneRoom(predicate: (room: Room) -> Boolean): Room? {
        return rooms.find(predicate)
    }

    fun findByArea(area: Area): List<Room> {
        return rooms.filter { it.area.name == area.name }
    }

    fun findRoomModels(area: Area): List<RoomModel> {
        return models.filterIsInstance<RoomModel>().filter { it.area.id == area.id }
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

    fun setRoomArea(id: Int, area: Area) {
        findOneRoom(id).area = area
        findOneModel(id).area = area
    }

    fun setRoomBrief(id: Int, brief: String) {
        findOneRoom(id).brief = brief
        findOneModel(id).name = brief
    }

    fun addToRoomDescription(id: Int, description: String) {
        findOneRoom(id).description += "\n$description"
        findOneModel(id).description += "\n$description"
    }

    fun changeRoomDescription(id: Int, substitute: String, lineNumber: Int) {
        val room = findOneRoom(id)
        val newDescription = changeLine(room.description, substitute, lineNumber)
        room.description = newDescription
        findOneModel(id).description = newDescription
    }

    fun removeRoomDescription(id: Int, lineNumber: Int) {
        val room = findOneRoom(id)
        val newDescription = removeLine(room.description, lineNumber)
        room.description = newDescription
        findOneModel(id).description = newDescription
    }

    private fun getAreaNextId(): Int {
        return areas.maxOf { it.id } + 1
    }

    private fun findOneRoom(id: Int): Room {
        return rooms.find { it.id == id }!!
    }

    private fun findOneModel(id: Int): RoomModel {
        return models.find { it.id == id } as RoomModel
    }
}
