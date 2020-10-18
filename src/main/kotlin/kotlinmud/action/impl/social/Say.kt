package kotlinmud.action.impl.social

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createSayMessage
import kotlinmud.io.factory.freeForm
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.type.Syntax
import kotlinmud.player.social.Social
import kotlinmud.player.social.SocialChannel
import kotlinx.coroutines.runBlocking

fun createSayAction(): Action {
    return Action(Command.SAY, mustBeAwake(), freeForm()) {
        val text = it.get<String>(Syntax.FREE_FORM)
        runBlocking {
            it.publishSocial(
                Social(
                    SocialChannel.SAY,
                    it.getMob(),
                    it.getRoom(),
                    createSayMessage(it.getMob(), text)
                )
            )
        }
        it.createOkResponse(messageToActionCreator("you say, \"$text\""))
    }
}
