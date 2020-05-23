package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.type.Command
import kotlinmud.io.messageToActionCreator
import kotlinmud.mob.type.JobType
import kotlinmud.string.leftPad

fun createListAction(): Action {
    return Action(Command.LIST) { svc ->
        val shopkeeper = svc.getMobsInRoom().find { it.job == JobType.SHOPKEEPER }
            ?: return@Action svc.createOkResponse(messageToActionCreator("There are no shopkeepers here."))
        svc.createOkResponse(
            messageToActionCreator(
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
}
