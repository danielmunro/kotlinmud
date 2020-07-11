package kotlinmud.player.social

import kotlinmud.io.model.Message
import kotlinmud.mob.dao.MobDAO
import kotlinmud.room.dao.RoomDAO

data class Social(
    val channel: SocialChannel,
    val mob: MobDAO,
    val room: RoomDAO,
    val message: Message,
    val target: MobDAO? = null
)
