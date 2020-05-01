package kotlinmud.action.impl.room.creation

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax

fun createRoomDescriptionAction(): Action {
    return Action(
        Command.ROOM_DESCRIPTION,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.SUBCOMMAND, Syntax.FREE_FORM),
        { svc: ActionContextService, request: Request ->
            val description = svc.get<String>(Syntax.FREE_FORM)
            svc.withRoomBuilding(request.mob) { it.description(description) }
            svc.createResponse(Message("the room builder description is now $description."))
        }
    )
}
