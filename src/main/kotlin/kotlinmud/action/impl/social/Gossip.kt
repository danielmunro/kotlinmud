package kotlinmud.action.impl.social

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlive
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.social.Social
import kotlinmud.social.SocialChannel

fun createGossipAction(): Action {
    return Action(
        Command.GOSSIP,
        mustBeAlive(),
        listOf(Syntax.COMMAND, Syntax.FREE_FORM)) {
            val text = it.get<String>(Syntax.FREE_FORM)
            it.publishSocial(
                Social(
                    SocialChannel.GOSSIP,
                    it.getMob(),
                    it.getRoom(),
                    Message(
                        "you gossip, \"$text\"",
                        "${it.getMob()} gossips, \"$text\""
                    )
                )
            )
            it.createResponse(Message("you gossip, \"$text\""))
        }
}
