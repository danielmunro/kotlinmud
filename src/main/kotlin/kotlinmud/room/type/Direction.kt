package kotlinmud.room.type

enum class Direction(val value: String) {
    NORTH("north"),
    SOUTH("south"),
    EAST("east"),
    WEST("west"),
    UP("up"),
    DOWN("down"),
}

fun getReverseDirection(direction: Direction): Direction {
    return when (direction) {
        Direction.NORTH -> Direction.SOUTH
        Direction.SOUTH -> Direction.NORTH
        Direction.EAST -> Direction.WEST
        Direction.WEST -> Direction.EAST
        Direction.UP -> Direction.DOWN
        Direction.DOWN -> Direction.UP
    }
}
