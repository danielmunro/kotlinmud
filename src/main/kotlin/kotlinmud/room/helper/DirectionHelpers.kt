package kotlinmud.room.helper

import kotlinmud.room.type.Direction

fun matchDirectionString(input: String): Direction? {
    return Direction.values().find { it.value.startsWith(input) }
}

fun getDirectionString(direction: Direction): String {
    return when (direction) {
        Direction.DOWN -> "below you"
        Direction.UP -> "above you"
        Direction.EAST -> "to the east"
        Direction.WEST -> "to the west"
        Direction.NORTH -> "to the north"
        Direction.SOUTH -> "to the south"
    }
}
