package kotlinmud.action.impl.social

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeAwake
import kotlinmud.action.type.Command
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.factory.freeForm
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.type.Syntax
import kotlinmud.player.social.Social
import kotlinmud.player.social.SocialChannel

fun createSayAction(): Action {
    return Action(Command.SAY, mustBeAwake(), freeForm()) {
        val text = it.get<String>(Syntax.FREE_FORM)
        it.publishSocial(
            Social(
                SocialChannel.SAY,
                it.getMob(),
                it.getRoom(),
                MessageBuilder()
                    .toTarget("${it.getMob()} says, \"$text\"")
                    .build()
            )
        )
        it.createOkResponse(messageToActionCreator("you say, \"$text\""))
    }
}
