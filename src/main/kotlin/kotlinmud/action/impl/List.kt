package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.JobType
import kotlinmud.string.leftPad

fun createListAction(): Action {
    return Action(
        Command.LIST,
        mustBeAlert(),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, request: Request ->
            val shopkeeper = svc.getMobsInRoom(request.room).find { it.job == JobType.SHOPKEEPER }!!
            svc.createResponse(
                Message(
                    "[lvl cost name]\n" +
                    shopkeeper.inventory.getItemGroups().map {
                        val level = it.value[0].level.toString()
                        val cost = it.value[0].value.toString()
                        leftPad(level, 5 - level.length) +
                                " " + leftPad(cost, 6 - cost.length) +
                                " " + it.value[0].name
                    }.fold("") {
                        acc: String, it: String -> "$acc\n$it"
                    }
                )
            )
        }
    )
}