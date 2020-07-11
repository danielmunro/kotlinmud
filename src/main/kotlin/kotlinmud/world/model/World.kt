package kotlinmud.world.model

import kotlinmud.fs.loader.area.model.reset.ItemMobReset
import kotlinmud.fs.loader.area.model.reset.ItemRoomReset
import kotlinmud.fs.loader.area.model.reset.MobReset
import kotlinmud.item.dao.ItemDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.room.dao.DoorDAO
import kotlinmud.room.dao.RoomDAO

data class World(
    var rooms: List<RoomDAO>,
    var doors: List<DoorDAO>,
    var mobs: List<MobDAO>,
    var items: List<ItemDAO>,
    var mobResets: List<MobReset>,
    var itemMobResets: List<ItemMobReset>,
    var itemRoomResets: List<ItemRoomReset>
)
