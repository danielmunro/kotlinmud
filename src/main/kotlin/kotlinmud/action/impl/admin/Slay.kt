package kotlinmud.action.impl.admin

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.mobInRoom
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob
import kotlinmud.mob.type.Role
import kotlinx.coroutines.runBlocking

fun createSlayAction(): Action {
    return ActionBuilder(Command.SLAY).also {
        it.minimumRole = Role.Admin
        it.syntax = mobInRoom()
    }.build {
        val target = it.get<Mob>(Syntax.MOB_IN_ROOM)
        runBlocking { it.createFight(target) }
        target.hp = -1
        it.createOkResponse(messageToActionCreator("you slay $target in cold blood!"))
    }
}
