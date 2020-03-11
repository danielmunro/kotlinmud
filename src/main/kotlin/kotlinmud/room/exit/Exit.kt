package kotlinmud.room.exit

import kotlinmud.room.Direction
import kotlinmud.room.Room

data class Exit(val destination: Room, val direction: Direction, val door: Door? = null)
