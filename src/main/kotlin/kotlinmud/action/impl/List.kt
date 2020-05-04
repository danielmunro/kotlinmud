package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.mob.JobType
import kotlinmud.string.leftPad

fun createListAction(): Action {
    return Action(
        Command.LIST,
        mustBeAlert(),
        listOf(Syntax.COMMAND),
        { svc ->
            val shopkeeper = svc.getMobsInRoom(svc.getRoom()).find { it.job == JobType.SHOPKEEPER }
                ?: return@Action svc.createResponse(Message("There are no shopkeepers here."))
                svc.createResponse(
                    Message(
                        "[lvl cost name]\n" +
                            svc.getItemGroupsFor(shopkeeper).map {
                                val level = it.value[0].level.toString()
                                val cost = it.value[0].worth.toString()
                                leftPad(level, 3) +
                                        " " + leftPad(cost, 3) +
                                        " " + it.value[0].name
                            }.joinToString("\n")
                    )
                )
        }
    )
}
