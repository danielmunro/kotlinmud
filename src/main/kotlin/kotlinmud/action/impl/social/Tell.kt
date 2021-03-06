package kotlinmud.action.impl.social

import kotlinmud.action.helper.mustBeAlive
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createTellMessage
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.playerFreeForm
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob
import kotlinmud.player.social.Social
import kotlinmud.player.social.SocialChannel
import kotlinx.coroutines.runBlocking

fun createTellAction(): Action {
    return Action(Command.TELL, mustBeAlive(), playerFreeForm()) {
        val text = it.get<String>(Syntax.FREE_FORM)
        val target = it.get<Mob>(Syntax.PLAYER_MOB)
        runBlocking {
            it.publishSocial(
                Social(
                    SocialChannel.TELL,
                    it.getMob(),
                    it.getRoom(),
                    createTellMessage(it.getMob(), text),
                    target
                )
            )
        }
        it.createOkResponse(messageToActionCreator("you tell $target, \"$text\""))
    }
}
