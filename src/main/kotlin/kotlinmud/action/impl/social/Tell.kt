package kotlinmud.action.impl.social

import kotlinmud.action.helper.mustBeAlive
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createTellMessage
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.playerFreeForm
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.player.social.Social
import kotlinmud.player.social.SocialChannel

fun createTellAction(): Action {
    return Action(Command.TELL, mustBeAlive(), playerFreeForm()) {
        val text = it.get<String>(Syntax.FREE_FORM)
        val target = it.get<MobDAO>(Syntax.PLAYER_MOB)
        it.publishSocial(
            Social(
                SocialChannel.TELL,
                it.getMob(),
                it.getRoom(),
                createTellMessage(it.getMob(), text),
                target
            )
        )
        it.createOkResponse(messageToActionCreator("you tell $target, \"$text\""))
    }
}
