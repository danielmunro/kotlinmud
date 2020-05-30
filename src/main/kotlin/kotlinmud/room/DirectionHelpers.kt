package kotlinmud.room

import kotlinmud.room.type.Direction

fun matchDirectionString(input: String): Direction? {
    return Direction.values().find { it.value.startsWith(input) }
}

fun oppositeDirection(direction: Direction): Direction {
    return when (direction) {
        Direction.NORTH -> Direction.SOUTH
        Direction.SOUTH -> Direction.NORTH
        Direction.WEST -> Direction.EAST
        Direction.EAST -> Direction.WEST
        Direction.UP -> Direction.DOWN
        Direction.DOWN -> Direction.UP
    }
}
