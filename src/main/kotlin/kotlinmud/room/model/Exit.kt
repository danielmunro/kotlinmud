package kotlinmud.room.model

import kotlinmud.room.type.Direction

data class Exit(val destination: Room, val direction: Direction, val door: Door? = null)
