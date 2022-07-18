package kotlinmud.room.builder

import kotlinmud.biome.type.ResourceType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.item.model.Item
import kotlinmud.item.type.HasInventory
import kotlinmud.mob.model.Mob
import kotlinmud.room.model.Area
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.RegenLevel

class RoomBuilder(private val roomService: RoomService) : HasInventory {
    var id: Int = 0
    lateinit var name: String
    var brief = ""
    lateinit var description: String
    var area: Area? = null
    var isIndoors = false
    var regenLevel = RegenLevel.NORMAL
    var substrate = SubstrateType.DIRT
    var elevation = 0
    override var items = mutableListOf<Item>()
    var resources = listOf<ResourceType>()
    var owner: Mob? = null
    var north: Room? = null
    var south: Room? = null
    var east: Room? = null
    var west: Room? = null
    var up: Room? = null
    var down: Room? = null

    fun setFromKeyword(keyword: String, value: String) {
        when (keyword) {
            "regen" -> regenLevel = RegenLevel.valueOf(value.uppercase())
        }
    }

    fun build(): Room {
        val room = Room(
            id,
            name,
            brief,
            description,
            area!!,
            regenLevel,
            substrate,
            elevation,
            items.toMutableList(),
            resources.toMutableList(),
            owner,
        )
        room.north = north
        room.south = south
        room.east = east
        room.west = west
        room.up = up
        room.down = down
        roomService.add(room)
        return room
    }
}
