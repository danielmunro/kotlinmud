package kotlinmud.action.impl.admin

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createUnbanMessage
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.playerMob
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.type.Role
import kotlinmud.mob.type.Standing

fun createUnbanAction(): Action {
    return ActionBuilder(Command.UNBAN).also {
        it.minimumRole = Role.Admin
        it.syntax = playerMob()
    } build {
        val target = it.get<PlayerMob>(Syntax.PLAYER_MOB)
        if (target.standing == Standing.Good) {
            return@build it.createErrorResponse(messageToActionCreator("They are already in good standing."))
        }
        target.standing = Standing.Good
        it.createOkResponse(createUnbanMessage(target))
    }
}
