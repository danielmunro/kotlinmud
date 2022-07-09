package kotlinmud.persistence.dumper

import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.spec.RoomSpec
import kotlinmud.room.model.Area
import kotlinmud.room.model.Room

class RoomDumperService(area: Area, private val rooms: List<Room>) {
    private val roomSpec = RoomSpec(area)

    fun dump(): String {
        var buffer = if (rooms.isNotEmpty()) "rooms:\n" else ""
        rooms.forEach { room ->
            roomSpec.tokens.forEach { token ->
                buffer += when (token.token) {
                    TokenType.ID -> "${room.id}. "
                    TokenType.Name -> room.name + "\n"
                    TokenType.Description -> room.description + "~\n"
                    TokenType.Props -> dumpProps(room)
                    else -> ""
                }
            }
            buffer += "\n"
        }
        return buffer
    }

    private fun dumpProps(room: Room): String {
        return """north ${room.north?.id ?: 0}~
south ${room.south?.id ?: 0}~
east ${room.east?.id ?: 0}~
west ${room.west?.id ?: 0}~
up ${room.up?.id ?: 0}~
down ${room.down?.id ?: 0}~
~
"""
    }
}
