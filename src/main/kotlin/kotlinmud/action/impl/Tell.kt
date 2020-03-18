package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlive
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Social
import kotlinmud.io.SocialChannel
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob

fun createTellAction(): Action {
    return Action(
        Command.TELL,
        mustBeAlive(),
        listOf(Syntax.COMMAND, Syntax.PLAYER_MOB, Syntax.FREE_FORM),
        { svc: ActionContextService, request: Request ->
            val text = svc.get<String>(Syntax.FREE_FORM)
            val target = svc.get<Mob>(Syntax.PLAYER_MOB)
            val message = Message(
                "you tell $target, \"$text\"",
                "${request.mob} tells you, \"$text\""
            )
            svc.publishSocial(
                Social(
                    SocialChannel.TELL,
                    request.mob,
                    request.room,
                    message,
                    target
                )
            )
            svc.createResponse(message)
        })
}
