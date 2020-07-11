package kotlinmud.action.contextBuilder

import kotlinmud.action.factory.failedContext
import kotlinmud.action.model.Context
import kotlinmud.action.type.Exit
import kotlinmud.action.type.Status
import kotlinmud.biome.type.SubstrateType
import kotlinmud.io.type.Syntax
import kotlinmud.room.dao.RoomDAO

class DirectionToExitContextBuilder(private val room: RoomDAO) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val exit = getExit(word)
            ?: return failedContext(syntax, "Alas, that direction does not exist.")

        if (!exit.value.isDoorPassable(exit.key)) {
            return failedContext(syntax, "you must open the door first.")
        }

        if (!isClimbableElevation(exit)) {
            return Context(syntax, Status.ERROR, "you can't climb that elevation.")
        }

        if (!canMoveIntoSubstrate(exit.value)) {
            return Context(syntax, Status.ERROR, "${exit.value.name} is blocked by ${exit.value.substrate.toString().toLowerCase()}.")
        }

        return Context(syntax, Status.OK, exit.value)
    }

    private fun getExit(word: String): Exit? {
        return room.getAllExits().entries.find {
            it.key.toString().toLowerCase().startsWith(word)
        }
    }

    private fun isClimbableElevation(exit: Exit): Boolean {
        return room.isElevationPassable(exit.key)
    }

    private fun canMoveIntoSubstrate(room: RoomDAO): Boolean {
        return room.substrate == SubstrateType.NONE
    }
}
