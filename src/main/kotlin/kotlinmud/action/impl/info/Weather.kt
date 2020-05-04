package kotlinmud.action.impl.info

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext

fun createWeatherAction(): Action {
    return Action(
        Command.WEATHER,
        mustBeAlert(),
        listOf(Syntax.COMMAND)) {
            if (it.getRoom().isIndoor) {
                return@Action createResponseWithEmptyActionContext(
                    Message("You can't see the weather indoors.")
                )
            }
            createResponseWithEmptyActionContext(
                Message(
                    "")
            )
        }
}
