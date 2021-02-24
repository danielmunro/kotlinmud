package kotlinmud.player.social

import kotlinmud.io.model.Message
import kotlinmud.mob.model.Mob
import kotlinmud.room.dao.RoomDAO

data class Social(
    val channel: SocialChannel,
    val mob: Mob,
    val room: RoomDAO,
    val message: Message,
    val target: Mob? = null
)
