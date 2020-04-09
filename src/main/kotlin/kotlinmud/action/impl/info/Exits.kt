package kotlinmud.action.impl.info

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext
import kotlinmud.world.room.exit.DoorDisposition

fun createExitsAction(): Action {
    return Action(
        Command.EXITS,
        mustBeAlert(),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, request: Request ->
            createResponseWithEmptyActionContext(
                Message("Exits include:\n${request.room.exits.fold("") {
                    acc, it ->
                    acc + if (it.door != null && it.door.disposition != DoorDisposition.OPEN) "\n${it.direction.value} - ${it.door}" else "\n${it.direction.value} - ${it.destination.name}"
                }}")
            )
        })
}
