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
    private lateinit var name: String
    private lateinit var description: String
    private lateinit var area: Area
    private var canonicalId: RoomCanonicalId? = null
    private var isIndoors = false
    private var regenLevel = RegenLevel.NORMAL
    private var biome = BiomeType.PLAINS
    private var substrate = SubstrateType.NONE
    private var elevation = 0
    private var maxWeight = 10000
    private var maxItems = 1000
    private var items = listOf<Item>()
    private var resources = listOf<ResourceType>()
    private var owner: Mob? = null
    private var north: Room? = null
    private var south: Room? = null
    private var east: Room? = null
    private var west: Room? = null
    private var up: Room? = null
    private var down: Room? = null

    fun name(value: String): RoomBuilder {
        name = value
        return this
    }

    fun description(value: String): RoomBuilder {
        description = value
        return this
    }

    fun area(value: Area): RoomBuilder {
        area = value
        return this
    }

    fun canonicalId(value: RoomCanonicalId?): RoomBuilder {
        canonicalId = value
        return this
    }

    fun isIndoors(value: Boolean): RoomBuilder {
        isIndoors = value
        return this
    }

    fun regenLevel(value: RegenLevel): RoomBuilder {
        regenLevel = value
        return this
    }

    fun elevation(value: Int): RoomBuilder {
        elevation = value
        return this
    }

    fun substrate(value: SubstrateType): RoomBuilder {
        substrate = value
        return this
    }

    fun north(value: Room): RoomBuilder {
        north = value
        return this
    }

    fun south(value: Room): RoomBuilder {
        south = value
        return this
    }

    fun east(value: Room): RoomBuilder {
        east = value
        return this
    }

    fun west(value: Room): RoomBuilder {
        west = value
        return this
    }

    fun up(value: Room): RoomBuilder {
        up = value
        return this
    }

    fun down(value: Room): RoomBuilder {
        down = value
        return this
    }

    fun build(): Room {
        val room = Room(
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
