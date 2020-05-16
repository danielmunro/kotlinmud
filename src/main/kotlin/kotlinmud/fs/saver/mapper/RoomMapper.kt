package kotlinmud.fs.saver.mapper

import kotlinmud.world.room.Room

fun mapRoom(room: Room): String {
    return """#${room.id}
${room.name}~
${room.description}~
${room.area}~
${room.biome.value}~
ownerId: ${room.owner?.id ?: 0}, ${room.exits.joinToString(", ") {
        it.direction.value[0] + ": " + (if (it.door != null) "door-${it.door.id}-" else "") + it.destination.id
    }}~
"""
}
