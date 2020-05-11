package kotlinmud.world

import kotlinmud.data.Table
import kotlinmud.fs.loader.area.mapper.RoomMapper
import kotlinmud.fs.loader.area.model.reset.ItemMobReset
import kotlinmud.fs.loader.area.model.reset.ItemRoomReset
import kotlinmud.fs.loader.area.model.reset.MobReset
import kotlinmud.item.Item
import kotlinmud.mob.Mob
import kotlinmud.world.room.Room
import kotlinmud.world.room.RoomBuilder
import kotlinmud.world.room.exit.Door

data class World(private val areas: List<Area>) {
    var rooms: Table<Room>
    var doors: Table<Door>
    var mobs: Table<Mob>
    var items: Table<Item>
    var mobResets: Table<MobReset>
    var itemMobResets: Table<ItemMobReset>
    var itemRoomResets: Table<ItemRoomReset>
    var nextRoomID = 0
    val biomes = createBiomes()

    init {
        val allRoomModels = areas.flatMap { it.roomMapper.roomModels }
        val allDoors = areas.flatMap { it.roomMapper.doors }
        mobs = Table(areas.flatMap { it.mobs }.toMutableList())
        val allRoomsMapper = RoomMapper(biomes, mobs.toList(), allRoomModels, allDoors)
        doors = Table(allDoors.toMutableList())
        rooms = Table(allRoomsMapper.map().toMutableList())
        items = Table(areas.flatMap { it.items }.toMutableList())
        mobResets = Table(areas.flatMap { it.mobResets }.toMutableList())
        itemMobResets = Table(areas.flatMap { it.itemMobResets }.toMutableList())
        itemRoomResets = Table(areas.flatMap { it.itemRoomResets }.toMutableList())
        nextRoomID = rooms.toList().size
    }

    fun createRoomBuilder(): RoomBuilder {
        nextRoomID++
        return RoomBuilder()
            .id(nextRoomID)
            .exits(mutableListOf())
    }
}
