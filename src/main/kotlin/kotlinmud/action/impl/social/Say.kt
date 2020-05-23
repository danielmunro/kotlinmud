package kotlinmud.action.impl.social

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.freeForm
import kotlinmud.io.messageToActionCreator
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
