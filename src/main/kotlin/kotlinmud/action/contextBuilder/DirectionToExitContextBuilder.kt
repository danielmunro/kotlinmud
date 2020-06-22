package kotlinmud.action.contextBuilder

import kotlinmud.action.factory.failedContext
import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.MAX_WALKABLE_ELEVATION
import kotlinmud.room.model.Exit
import kotlinmud.room.model.Room
import kotlinmud.room.type.DoorDisposition

class DirectionToExitContextBuilder(private val room: Room) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val exit = getValidDirection(word)
            ?: return failedContext(syntax, "Alas, that direction does not exist.")

        if (!exitHasNoDoorOrDoorIsOpen(exit)) {
            return failedContext(syntax, "you must open the door first.")
        }

        if (!isClimbableElevation(exit)) {
            return Context(syntax, Status.ERROR, "you can't climb that elevation.")
        }

        return Context(syntax, Status.OK, exit.destination)
    }

    private fun getValidDirection(word: String): Exit? {
        return room.exits.find { it.direction.toString().toLowerCase().startsWith(word) }
    }

    private fun exitHasNoDoorOrDoorIsOpen(exit: Exit): Boolean {
        return exit.door == null || exit.door.disposition == DoorDisposition.OPEN
    }

    private fun isClimbableElevation(exit: Exit): Boolean {
        val elevationChange = exit.destination.elevation - room.elevation

        return elevationChange <= MAX_WALKABLE_ELEVATION
    }
}
