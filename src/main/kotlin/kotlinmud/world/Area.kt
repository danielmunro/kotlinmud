package kotlinmud.world

import kotlinmud.fs.loader.area.mapper.RoomMapper
import kotlinmud.fs.loader.area.model.reset.ItemMobReset
import kotlinmud.fs.loader.area.model.reset.ItemRoomReset
import kotlinmud.fs.loader.area.model.reset.MobReset
import kotlinmud.item.Item
import kotlinmud.mob.Mob

data class Area(
    val baseDir: String,
    val roomMapper: RoomMapper,
    val items: List<Item>,
    val mobs: List<Mob>,
    val mobResets: List<MobReset>,
    val itemMobResets: List<ItemMobReset>,
    val itemRoomResets: List<ItemRoomReset>
)
