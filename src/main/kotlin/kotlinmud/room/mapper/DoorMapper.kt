package kotlinmud.room.mapper

import kotlinmud.fs.helper.int
import kotlinmud.fs.helper.str
import kotlinmud.room.model.Door

fun mapDoor(door: Door): String {
    return """${int(door.id)}
${str(door.name)}
${str(door.description)}
${str(door.disposition.toString().toLowerCase())}
"""
}
