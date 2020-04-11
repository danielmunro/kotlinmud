package kotlinmud.action.impl.social

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlive
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.social.Social
import kotlinmud.social.SocialChannel

fun createGossipAction(): Action {
    return Action(
        Command.GOSSIP,
        mustBeAlive(),
        listOf(Syntax.COMMAND, Syntax.FREE_FORM),
        { svc: ActionContextService, request: Request ->
            val text = svc.get<String>(Syntax.FREE_FORM)
            svc.publishSocial(
                Social(
                    SocialChannel.GOSSIP,
                    request.mob,
                    request.room,
                    Message(
                        "you gossip, \"$text\"",
                        "${request.mob} gossips, \"$text\""
                    )
                )
            )
            svc.createResponse(Message("you gossip, \"$text\""))
        })
}
