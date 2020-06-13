package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.room.model.Exit
import kotlinmud.room.model.Room

class DoorInRoomContextBuilder(private val room: Room) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return room.exits.find {
            it.door != null && matchExit(it, word)
        }?.let { Context<Any>(syntax, Status.OK, it.door!!) }
            ?: Context<Any>(
                syntax,
                Status.FAILED,
                "you don't see that anywhere."
            )
    }
}

fun matchExit(exit: Exit, word: String): Boolean {
    if (matches(exit.door!!.name, word)) {
        return true
    }

    if (matches(exit.direction.value, word)) {
        return true
    }

    return false
}
