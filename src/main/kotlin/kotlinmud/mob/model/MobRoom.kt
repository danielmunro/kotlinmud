package kotlinmud.mob.model

import kotlinmud.item.model.Item
import kotlinmud.mob.type.Disposition
import kotlinmud.world.room.Room

data class MobRoom(
    val mob: Mob,
    var room: Room,
    var disposition: Disposition = Disposition.STANDING,
    var furniture: Item? = null
)
