package kotlinmud.action.impl.social

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeAlive
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.playerFreeForm
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob
import kotlinmud.player.social.Social
import kotlinmud.player.social.SocialChannel

fun createTellAction(): Action {
    return Action(Command.TELL, mustBeAlive(), playerFreeForm()) {
        val text = it.get<String>(Syntax.FREE_FORM)
        val target = it.get<Mob>(Syntax.PLAYER_MOB)
        it.publishSocial(
            Social(
                SocialChannel.TELL,
                it.getMob(),
                it.getRoom(),
                MessageBuilder()
                    .toTarget("${it.getMob()} tells you, \"$text\"")
                    .build(),
                target
            )
        )
        it.createOkResponse(messageToActionCreator("you tell $target, \"$text\""))
    }
}
