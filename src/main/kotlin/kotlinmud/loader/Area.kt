package kotlinmud.loader

import kotlinmud.item.Item
import kotlinmud.loader.mapper.RoomMapper
import kotlinmud.loader.model.reset.MobReset
import kotlinmud.mob.Mob

class Area(
    val id: String,
    val roomMapper: RoomMapper,
    val items: List<Item>,
    val mobs: List<Mob>,
    val mobResets: List<MobReset>
)
