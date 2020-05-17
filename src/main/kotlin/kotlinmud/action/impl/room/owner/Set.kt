package kotlinmud.action.impl.room.owner

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.subcommandPlayerMob
import kotlinmud.mob.Mob

fun createOwnerSetAction(): Action {
    return Action(Command.OWNER_SET, mustBeAlert(), subcommandPlayerMob()) {
        val mob = it.get<Mob>(Syntax.PLAYER_MOB)
        it.getRoom().owner = mob
        it.createResponse(
            MessageBuilder()
                .toActionCreator("you have made $mob the new owner of this room.")
                .toObservers("${it.getMob()} has made $mob the new owner of this room.")
                .build()
        )
    }
}
