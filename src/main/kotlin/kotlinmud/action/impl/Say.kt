package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Social
import kotlinmud.io.SocialChannel
import kotlinmud.io.Syntax

fun createSayAction(): Action {
    return Action(
        Command.SAY,
        mustBeAwake(),
        listOf(Syntax.COMMAND, Syntax.FREE_FORM),
        { svc: ActionContextService, request: Request ->
            val text = svc.get<String>(Syntax.FREE_FORM)
            svc.publishSocial(
                Social(
                    SocialChannel.SAY,
                    request.mob,
                    request.room,
                    Message(
                        "you say, \"$text\"",
                        "${request.mob} says, \"$text\""
                    )
                )
            )
            svc.createResponse(Message("you say, \"$text\""))
        })
}
