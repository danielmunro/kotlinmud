package kotlinmud.action.impl.social

import kotlinmud.action.helper.mustBeAlive
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.freeForm
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
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
