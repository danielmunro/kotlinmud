package kotlinmud.room.mapper

import kotlinmud.fs.int
import kotlinmud.fs.str
import kotlinmud.room.model.Room

fun mapRoom(room: Room): String {
    return """${int(room.id)}
${str(room.name)}
${str(room.description)}
${str(room.area)}
${str(room.biome.value)}
${int(room.elevation)}
${str(room.resources.joinToString(", ") { it.value })}
${str("ownerId: ${room.owner?.id ?: 0}, ${room.exits.joinToString(", ") {
        it.direction.value[0] + ": " + (if (it.door != null) "door-${it.door.id}-" else "") + it.destination.id
    }}")}
"""
}
