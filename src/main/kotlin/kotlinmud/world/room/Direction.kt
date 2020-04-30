package kotlinmud.world.room

enum class Direction(val value: String) {
    NORTH("north"),
    SOUTH("south"),
    EAST("east"),
    WEST("west"),
    UP("up"),
    DOWN("down"),
}

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
