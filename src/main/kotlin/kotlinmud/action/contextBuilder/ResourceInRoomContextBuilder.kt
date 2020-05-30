package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.Syntax
import kotlinmud.room.model.Room
import kotlinmud.string.matches

class ResourceInRoomContextBuilder(private val room: Room) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return room.resources.find {
            matches(it.value, word)
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(
            syntax,
            Status.ERROR,
            "you don't see that anywhere."
        )
    }
}
