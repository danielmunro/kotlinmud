package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeStanding
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.mobInRoom
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO

fun createKillAction(): Action {
    return Action(Command.KILL, mustBeStanding(), mobInRoom()) {
        val target = it.get<MobDAO>(Syntax.MOB_IN_ROOM)
        it.createFight()
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you scream and attack $target!")
                .toTarget("${it.getMob()} screams and attacks you!")
                .toObservers("${it.getMob()} screams and attacks $target!")
                .build()
        )
    }
}
