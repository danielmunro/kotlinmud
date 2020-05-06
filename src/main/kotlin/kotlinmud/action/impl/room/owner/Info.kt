package kotlinmud.action.impl.room.owner

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.subcommand

fun createOwnerInfoAction(): Action {
    return Action(Command.OWNER_INFO, mustBeAlert(), subcommand()) {
        it.createResponse(Message("this room is owned by ${it.getRoom().owner ?: "no one"}."))
    }
}
