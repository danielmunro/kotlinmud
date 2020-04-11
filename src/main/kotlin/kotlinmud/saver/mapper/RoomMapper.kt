package kotlinmud.saver.mapper

import kotlinmud.world.room.Room

fun mapRoom(room: Room): String {
    return """#${room.id}
${room.name}~
${room.description}~
${room.exits.joinToString(", ") {
        it.direction.value[0] + ": " + (if (it.door != null) "door-${it.door.id}-" else "") + it.destination.id
    }}~
"""
}
