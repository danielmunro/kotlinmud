package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.io.Syntax
import kotlinmud.room.RoomEntity
import org.jetbrains.exposed.sql.transactions.transaction

class DirectionToExitContextBuilder(private val roomEntity: RoomEntity) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val room = transaction {
            roomEntity.exits.find { it.direction.toString().toLowerCase().startsWith(word) }?.destination
        }
        if (room != null) {
            return Context(syntax, Status.OK, room)
        }
        return Context(syntax, Status.FAILED, "Alas, that direction does not exist.")
    }
}
