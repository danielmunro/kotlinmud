package kotlinmud.action.impl.admin

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.playerMob
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.type.Role

fun createBoomAction(): Action {
    return ActionBuilder(Command.BOOM).also {
        it.minimumRole = Role.Admin
        it.syntax = playerMob()
    } build {
        val target = it.get<PlayerMob>(Syntax.PLAYER_MOB)
        target.hp = target.calc(Attribute.HP)
        it.createOkResponse(messageToActionCreator("boom!"))
    }
}
