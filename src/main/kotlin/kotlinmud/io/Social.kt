package kotlinmud.io

import kotlinmud.mob.Mob
import kotlinmud.room.Room

data class Social(
    val channel: SocialChannel,
    val mob: Mob,
    val room: Room,
    val message: Message,
    val target: Mob? = null
)
