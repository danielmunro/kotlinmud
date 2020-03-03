package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.io.*

fun createSayAction(): Action {
    return Action(
        Command.SAY,
        mustBeAwake(),
        listOf(Syntax.COMMAND, Syntax.FREE_FORM),
        { svc: ActionContextService, request: Request ->
            val text = svc.get<String>(Syntax.FREE_FORM)
            val message = Message(
                "you say, \"$text\"",
                "${request.mob} says, \"$text\""
            )
            svc.publishSocial(
                Social(
                    SocialChannel.SAY,
                    request.mob,
                    request.room,
                    message
                )
            )
            svc.createResponse(message)
        })
}
