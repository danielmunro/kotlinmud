package kotlinmud.world

import kotlinmud.item.Item
import kotlinmud.fs.loader.mapper.RoomMapper
import kotlinmud.fs.loader.model.reset.ItemMobReset
import kotlinmud.fs.loader.model.reset.ItemRoomReset
import kotlinmud.fs.loader.model.reset.MobReset
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
