package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.io.Syntax
import kotlinmud.room.Room
import kotlinmud.room.exit.Exit
import kotlinmud.string.matches

class DoorInRoomContextBuilder(private val room: Room) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return room.exits.find {
            it.door != null && matchExit(it, word)
        }?.let { Context<Any>(syntax, Status.OK, it.door!!) }
            ?: Context<Any>(syntax, Status.FAILED, "you don't see that anywhere.")
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
