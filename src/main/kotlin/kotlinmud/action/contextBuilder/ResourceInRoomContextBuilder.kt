package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.model.Room

class ResourceInRoomContextBuilder(private val room: RoomDAO) : ContextBuilder {
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
