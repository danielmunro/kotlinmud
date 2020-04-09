package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.io.Syntax
import kotlinmud.world.room.Room
import kotlinmud.world.room.exit.DoorDisposition

class DirectionToExitContextBuilder(private val room: Room) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val exit = room.exits.find { it.direction.toString().toLowerCase().startsWith(word) }
            ?: return Context(syntax, Status.FAILED, "Alas, that direction does not exist.")

        if (exit.door != null && exit.door.disposition != DoorDisposition.OPEN) {
            return Context(syntax, Status.FAILED, "you must open the door first.")
        }

        return Context(syntax, Status.OK, exit.destination)
    }
}
