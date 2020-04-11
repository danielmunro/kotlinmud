package kotlinmud.fs.saver.mapper

import kotlinmud.world.room.exit.Door

fun mapDoor(door: Door): String {
    return """#${door.id}
${door.name}~
${door.description}~
${door.disposition.toString().toLowerCase()}~
"""
}
