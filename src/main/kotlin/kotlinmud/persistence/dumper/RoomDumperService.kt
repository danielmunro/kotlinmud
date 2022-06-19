package kotlinmud.persistence.dumper

import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.spec.RoomSpec
import kotlinmud.room.model.Room

class RoomDumperService(private val rooms: List<Room>) {
    companion object {
        val roomSpec = RoomSpec()
    }

    fun dump(): String {
        var buffer = if (rooms.isNotEmpty()) "rooms:\n" else ""
        rooms.forEach { room ->
            roomSpec.tokens.forEach { token ->
                buffer += when (token.token) {
                    TokenType.ID -> "${room.id}. "
                    TokenType.Name -> room.name + "\n"
                    TokenType.Description -> room.description + "~\n"
                    TokenType.Props -> "~\n"
                    else -> ""
                }
            }
            buffer += "\n"
        }
        return buffer
    }
}
