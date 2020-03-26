package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlive
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax

fun createWhoAction(): Action {
    return Action(
        Command.WHO,
        mustBeAlive(),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, _: Request ->
            svc.createResponse(
                Message(
                    "MORTALS\n---------------\n" +
                    svc.getClients().fold("") { acc, it ->
                        acc +
                                it.mob.level + " " +
                                it.mob.race.type.toString().toLowerCase() + " " +
                                it.mob.job.toString().toLowerCase() + " " +
                                it.mob.name + "\n"
                    }
                )
            )
        })
}