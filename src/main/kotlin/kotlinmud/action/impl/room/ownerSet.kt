package kotlinmud.action.impl.room

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob

fun createOwnerSetAction(): Action {
    return Action(
        Command.OWNER_SET,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.SUBCOMMAND, Syntax.PLAYER_MOB),
        { svc: ActionContextService, request: Request ->
            val mob = svc.get<Mob>(Syntax.PLAYER_MOB)
            request.room.owner = mob
            svc.createResponse(
                Message(
                    "you have made $mob the new owner of this room.",
                    "${request.mob} has made $mob the new owner of this room."
                )
            )
        }
    )
}
