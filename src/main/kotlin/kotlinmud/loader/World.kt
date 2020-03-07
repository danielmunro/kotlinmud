package kotlinmud.loader

import kotlinmud.data.Table
import kotlinmud.item.Item
import kotlinmud.loader.mapper.RoomMapper
import kotlinmud.loader.model.reset.ItemMobReset
import kotlinmud.loader.model.reset.MobReset
import kotlinmud.mob.Mob
import kotlinmud.room.Room

class World(areas: List<Area>) {
    var rooms: Table<Room>
    var mobs: Table<Mob>
    var items: Table<Item>
    var mobResets: Table<MobReset>
    var itemMobResets: Table<ItemMobReset>

    init {
        val allRoomModels = areas.flatMap { it.roomMapper.roomModels }
        val allDoors = areas.flatMap { it.roomMapper.doors }
        val allRoomsMapper = RoomMapper("world", allRoomModels, allDoors)
        rooms = Table(allRoomsMapper.map())
        mobs = Table(areas.flatMap { it.mobs })
        items = Table(areas.flatMap { it.items })
        mobResets = Table(areas.flatMap { it.mobResets })
        itemMobResets = Table(areas.flatMap { it.itemMobResets })
    }
}
