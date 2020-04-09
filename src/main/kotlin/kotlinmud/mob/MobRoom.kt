package kotlinmud.mob

import kotlinmud.world.room.Room

data class MobRoom(
    val mob: Mob,
    var room: Room
)
