package kotlinmud.world.model

import kotlinmud.data.Table
import kotlinmud.fs.loader.area.mapper.RoomMapper
import kotlinmud.fs.loader.area.model.reset.ItemMobReset
import kotlinmud.fs.loader.area.model.reset.ItemRoomReset
import kotlinmud.fs.loader.area.model.reset.MobReset
import kotlinmud.item.model.Item
import kotlinmud.mob.model.Mob
import kotlinmud.room.model.Door
import kotlinmud.room.model.Room
import kotlinmud.room.model.RoomBuilder

data class World(
    var rooms: Table<Room>,
    var doors: Table<Door>,
    var mobs: Table<Mob>,
    var items: Table<Item>,
    var mobResets: Table<MobReset>,
    var itemMobResets: Table<ItemMobReset>,
    var itemRoomResets: Table<ItemRoomReset>
) {
    companion object {
        fun fromAreas(areas: List<Area>): World {
            val allRoomModels = areas.flatMap { it.roomMapper.roomModels }
            val allDoors = areas.flatMap { it.roomMapper.doors }
            val mobs = Table(areas.flatMap { it.mobs }.toMutableList())
            val allRoomsMapper = RoomMapper(mobs.toList(), allRoomModels, allDoors)
            val doors = Table(allDoors.toMutableList())
            val rooms = Table(allRoomsMapper.map().toMutableList())
            val items = Table(areas.flatMap { it.items }.toMutableList())
            val mobResets = Table(areas.flatMap { it.mobResets }.toMutableList())
            val itemMobResets = Table(areas.flatMap { it.itemMobResets }.toMutableList())
            val itemRoomResets = Table(areas.flatMap { it.itemRoomResets }.toMutableList())
            return World(
                rooms,
                doors,
                mobs,
                items,
                mobResets,
                itemMobResets,
                itemRoomResets
            )
        }

        fun fromGenerator(world: kotlinmud.generator.model.World): World {
            return World(
                Table(world.rooms.toMutableList()),
                Table(mutableListOf()),
                Table(mutableListOf()),
                Table(mutableListOf()),
                Table(mutableListOf()),
                Table(mutableListOf()),
                Table(mutableListOf())
            )
        }
    }

    private var nextRoomID = rooms.toList().size

    fun createRoomBuilder(): RoomBuilder {
        nextRoomID++
        return RoomBuilder()
            .id(nextRoomID)
            .exits(mutableListOf())
    }
}
