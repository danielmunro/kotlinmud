package kotlinmud.action.impl.room.owner

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob

fun createOwnerSetAction(): Action {
    return Action(
        Command.OWNER_SET,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.SUBCOMMAND, Syntax.PLAYER_MOB)) {
            val mob = it.get<Mob>(Syntax.PLAYER_MOB)
            it.getRoom().owner = mob
            it.createResponse(
                Message(
                    "you have made $mob the new owner of this room.",
                    "${it.getMob()} has made $mob the new owner of this room."
                )
            )
        }
}
