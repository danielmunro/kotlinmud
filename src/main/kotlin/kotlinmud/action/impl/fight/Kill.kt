package kotlinmud.action.impl.fight

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.helper.mustBeStanding
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createKillMessage
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.mobInRoom
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob
import kotlinx.coroutines.runBlocking

fun createKillAction(): Action {
    return ActionBuilder(Command.KILL).also {
        it.dispositions = mustBeStanding()
        it.syntax = mobInRoom()
    } build {
        val target = it.get<Mob>(Syntax.MOB_IN_ROOM)
        if (!target.canTargetForFight()) {
            return@build it.createErrorResponse(messageToActionCreator("you cannot target them."))
        }
        runBlocking { it.createFight(target) }
        it.createOkResponse(createKillMessage(it.getMob(), target))
    }
}
