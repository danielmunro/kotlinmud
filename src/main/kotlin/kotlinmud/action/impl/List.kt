package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.JobType

fun createListAction(): Action {
    return Action(
        Command.LIST,
        mustBeAlert(),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, request: Request ->
            val shopkeeper = svc.getMobsInRoom(request.room).find { it.job == JobType.SHOPKEEPER }!!
            svc.createResponse(
                Message(
                    shopkeeper.inventory.items.map { "$it - ${it.value}" }.reduce {
                            acc: String, it: String -> "$acc\n$it"
                    }
                )
            )
        }
    )
}
