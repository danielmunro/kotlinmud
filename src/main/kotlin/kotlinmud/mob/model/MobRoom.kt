package kotlinmud.mob.model

import kotlinmud.item.dao.ItemDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.type.Disposition
import kotlinmud.room.dao.RoomDAO

data class MobRoom(
    val mob: MobDAO,
    var room: RoomDAO,
    var disposition: Disposition = Disposition.STANDING,
    var furniture: ItemDAO? = null
)
