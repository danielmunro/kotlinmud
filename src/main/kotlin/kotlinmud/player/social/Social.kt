package kotlinmud.player.social

import kotlinmud.io.Message
import kotlinmud.mob.Mob
import kotlinmud.world.room.Room

data class Social(
    val channel: SocialChannel,
    val mob: Mob,
    val room: Room,
    val message: Message,
    val target: Mob? = null
)
