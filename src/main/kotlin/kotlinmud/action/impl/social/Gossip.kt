package kotlinmud.action.impl.social

import kotlinmud.action.Action
import kotlinmud.action.mustBeAlive
import kotlinmud.action.type.Command
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.freeForm
import kotlinmud.io.messageToActionCreator
import kotlinmud.player.social.Social
import kotlinmud.player.social.SocialChannel

fun createGossipAction(): Action {
    return Action(Command.GOSSIP, mustBeAlive(), freeForm()) {
        val text = it.get<String>(Syntax.FREE_FORM)
        it.publishSocial(
            Social(
                SocialChannel.GOSSIP,
                it.getMob(),
                it.getRoom(),
                MessageBuilder()
                    .toTarget("${it.getMob()} gossips, \"$text\"")
                    .build()
            )
        )
        it.createOkResponse(messageToActionCreator("you gossip, \"$text\""))
    }
}
