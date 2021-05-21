package kotlinmud.room.builder

import kotlinmud.biome.type.BiomeType
import kotlinmud.biome.type.ResourceType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.item.model.Item
import kotlinmud.mob.model.Mob
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.RegenLevel
import kotlinmud.type.RoomCanonicalId

class RoomBuilder(private val roomService: RoomService) {
    lateinit var name: String
    lateinit var description: String
    lateinit var area: Area
    var id: Int = 0
    var canonicalId: RoomCanonicalId? = null
    var isIndoors = false
    var regenLevel = RegenLevel.NORMAL
    var biome = BiomeType.PLAINS
    var substrate = SubstrateType.NONE
    var elevation = 0
    var maxWeight = 10000
    var maxItems = 1000
    var items = listOf<Item>()
    var resources = listOf<ResourceType>()
    var owner: Mob? = null
    var north: Room? = null
    var south: Room? = null
    var east: Room? = null
    var west: Room? = null
    var up: Room? = null
    var down: Room? = null

    fun copy(modifier: (RoomBuilder) -> Unit): RoomBuilder {
        return RoomBuilder(roomService).also {
            it.name = name
            it.description = description
            it.area = area
            it.isIndoors = isIndoors
            it.regenLevel = regenLevel
            it.biome = biome
            it.substrate = substrate
            it.elevation = elevation
            it.maxWeight = maxWeight
            it.maxItems = maxItems
            it.owner = owner
        }.also(modifier)
    }

    fun build(): Room {
        val room = Room(
            id,
            name,
            description,
            canonicalId,
            area,
            isIndoors,
            regenLevel,
            biome,
            substrate,
            elevation,
            maxWeight,
            maxItems,
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
