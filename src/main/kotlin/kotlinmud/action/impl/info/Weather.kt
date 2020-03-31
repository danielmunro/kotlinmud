package kotlinmud.action.impl.info

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext

fun createWeatherAction(): Action {
    return Action(
        Command.WEATHER,
        mustBeAlert(),
        listOf(Syntax.COMMAND),
        { _: ActionContextService, request: Request ->
            if (request.room.isIndoor) {
                return@Action createResponseWithEmptyActionContext(
                    Message("You can't see the weather indoors.")
                )
            }
            createResponseWithEmptyActionContext(
                Message(
                    "")
            )
        })
}
