package kotlinmud.room.mapper

import kotlinmud.fs.int
import kotlinmud.fs.str
import kotlinmud.room.model.Door

fun mapDoor(door: Door): String {
    return """${int(door.id)}
${str(door.name)}
${str(door.description)}
${str(door.disposition.toString().toLowerCase())}
"""
}
