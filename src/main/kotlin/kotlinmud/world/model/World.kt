package kotlinmud.world.model

import kotlinmud.data.Table
import kotlinmud.fs.loader.area.model.reset.ItemMobReset
import kotlinmud.fs.loader.area.model.reset.ItemRoomReset
import kotlinmud.fs.loader.area.model.reset.MobReset
import kotlinmud.item.model.Item
import kotlinmud.mob.model.Mob
import kotlinmud.room.dao.DoorDAO
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.model.Door
import kotlinmud.room.model.Room
import kotlinmud.room.model.RoomBuilder

data class World(
    var rooms: List<RoomDAO>,
    var doors: List<DoorDAO>,
    var mobs: Table<Mob>,
    var items: Table<Item>,
    var mobResets: Table<MobReset>,
    var itemMobResets: Table<ItemMobReset>,
    var itemRoomResets: Table<ItemRoomReset>
) {
    private var nextRoomID = rooms.toList().size

    fun createRoomBuilder(): RoomBuilder {
        nextRoomID++
        return RoomBuilder()
            .id(nextRoomID)
            .exits(mutableListOf())
    }
}
