package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax
import kotlinmud.room.helper.matchDirectionString
import kotlinmud.room.model.Room

class DirectionWithNoExitContextBuilder(private val room: Room) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val direction = matchDirectionString(word) ?: return Context(
            syntax,
            Status.ERROR,
            "That is not a direction"
        )
        room.exits.find { it.direction == direction }?.let {
            return Context(
                syntax,
                Status.FAILED,
                "Alas, that direction already has a room."
            )
        }
        return Context(syntax, Status.OK, direction)
    }
}
