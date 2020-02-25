package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.io.Syntax
import kotlinmud.room.Room

class DoorInRoomContextBuilder(private val room: Room) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return room.exits.find { exit -> exit.door != null && kotlinmud.string.matches(exit.door.name, word) }
            ?.let { Context<Any>(syntax, Status.OK, it.door!!) }
            ?: Context<Any>(syntax, Status.FAILED, "you don't see that anywhere.")
    }
}
