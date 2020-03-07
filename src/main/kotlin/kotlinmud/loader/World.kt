package kotlinmud.loader

import kotlinmud.item.Item
import kotlinmud.loader.mapper.RoomMapper
import kotlinmud.loader.model.reset.ItemMobReset
import kotlinmud.loader.model.reset.MobReset
import kotlinmud.mob.Mob
import kotlinmud.room.Room

class World(areas: List<Area>) {
    var rooms: List<Room>
    var mobs: List<Mob>
    var items: List<Item>
    var mobResets: List<MobReset>
    var itemMobResets: List<ItemMobReset>

    init {
        val allRoomModels = areas.flatMap { it.roomMapper.roomModels }
        val allDoors = areas.flatMap { it.roomMapper.doors }
        val allRoomsMapper = RoomMapper("world", allRoomModels, allDoors)
        rooms = allRoomsMapper.map()
        mobs = areas.flatMap { it.mobs }
        items = areas.flatMap { it.items }
        mobResets = areas.flatMap { it.mobResets }
        itemMobResets = areas.flatMap { it.itemMobResets }
    }
}
