package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax
import kotlinmud.room.model.Room
import kotlinmud.room.type.DoorDisposition

class DirectionToExitContextBuilder(private val room: Room) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val exit = room.exits.find { it.direction.toString().toLowerCase().startsWith(word) }
            ?: return Context(
                syntax,
                Status.FAILED,
                "Alas, that direction does not exist."
            )

        if (exit.door != null && exit.door.disposition != DoorDisposition.OPEN) {
            return Context(
                syntax,
                Status.FAILED,
                "you must open the door first."
            )
        }

        val elevationChange = room.elevation - exit.destination.elevation

        if (elevationChange < -2) {
            return Context(syntax, Status.ERROR, "you can't climb that elevation.")
        }

        return Context(syntax, Status.OK, exit.destination)
    }
}
