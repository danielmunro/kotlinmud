package kotlinmud.action.impl.player

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.MessageBuilder

fun createLevelAction(): Action {
    return Action(Command.LEVEL, mustBeAlert()) {
        val mob = it.getMob()
        if (mob.getRemainingExperience() > 0) {
            return@Action it.createOkResponse(messageToActionCreator("you do not qualify for a level"))
        }
        mob.level += 1
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you gained a level!")
                .toObservers("$mob gained a level!")
                .build()
        )
    }
}
