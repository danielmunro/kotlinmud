package kotlinmud.action.impl.admin

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createBanMessage
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.playerMob
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.type.Role
import kotlinmud.mob.type.Standing

fun createBanAction(): Action {
    return ActionBuilder(Command.BAN).also {
        it.minimumRole = Role.Admin
        it.syntax = playerMob()
    } build {
        val target = it.get<PlayerMob>(Syntax.PLAYER_MOB)
        if (target.standing == Standing.Banned) {
            return@build it.createErrorResponse(messageToActionCreator("They are already banned."))
        }
        target.standing = Standing.Banned
        it.createOkResponse(createBanMessage(target))
    }
}
