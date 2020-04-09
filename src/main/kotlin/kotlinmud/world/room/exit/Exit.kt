package kotlinmud.world.room.exit

import kotlinmud.world.room.Direction
import kotlinmud.world.room.Room

data class Exit(val destination: Room, val direction: Direction, val door: Door? = null)
