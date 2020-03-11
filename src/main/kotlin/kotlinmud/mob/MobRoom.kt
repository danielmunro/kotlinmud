package kotlinmud.mob

import kotlinmud.room.Room

data class MobRoom(
    val mob: Mob,
    var room: Room
)
