package kotlinmud.room.helper

import kotlinmud.room.type.Direction

fun matchDirectionString(input: String): Direction? {
    return Direction.values().find { it.value.startsWith(input) }
}
