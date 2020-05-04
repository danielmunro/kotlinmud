package kotlinmud.action.impl.room.owner

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Syntax

fun createOwnerInfoAction(): Action {
    return Action(
        Command.OWNER_INFO,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.SUBCOMMAND),
        {
            it.createResponse(Message("this room is owned by ${it.getRoom().owner ?: "no one"}."))
        }
    )
}
