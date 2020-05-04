package kotlinmud.action.impl.social

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlive
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob
import kotlinmud.social.Social
import kotlinmud.social.SocialChannel

fun createTellAction(): Action {
    return Action(
        Command.TELL,
        mustBeAlive(),
        listOf(Syntax.COMMAND, Syntax.PLAYER_MOB, Syntax.FREE_FORM)) {
            val text = it.get<String>(Syntax.FREE_FORM)
            val target = it.get<Mob>(Syntax.PLAYER_MOB)
            it.publishSocial(
                Social(
                    SocialChannel.TELL,
                    it.getMob(),
                    it.getRoom(),
                    Message(
                        "you tell $target, \"$text\"",
                        "${it.getMob()} tells you, \"$text\""
                    ),
                    target
                )
            )
            it.createResponse(Message("you tell $target, \"$text\""))
        }
}
