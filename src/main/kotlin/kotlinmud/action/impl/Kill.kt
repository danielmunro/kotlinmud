package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob

fun createKillAction(): Action {
    return Action(
        Command.KILL,
        mustBeStanding(),
        listOf(Syntax.COMMAND, Syntax.MOB_IN_ROOM),
        {
            val target = it.get<Mob>(Syntax.MOB_IN_ROOM)
            it.createFightFor(it.getMob())
            it.createResponse(Message(
                    "you scream and attack $target!",
                    "${it.getMob()} screams and attacks you!",
                    "${it.getMob()} screams and attacks $target!"))
        })
}
