package kotlinmud.persistence.dumper

import kotlinmud.persistence.model.RoomModel
import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.spec.RoomSpec
import kotlinmud.room.model.Area

class RoomDumperService(area: Area, private val rooms: List<RoomModel>) {
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

    private fun dumpProps(room: RoomModel): String {
        return "${room.keywords.map { entry -> "${entry.key} ${entry.value}~" }.joinToString("\n")}\n~\n"
    }
}
