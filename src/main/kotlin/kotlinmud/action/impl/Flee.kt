package kotlinmud.action.impl

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeFighting
import kotlinmud.action.type.Command
import kotlinmud.io.MessageBuilder
import kotlinmud.io.command
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType

fun createFleeAction(): Action {
    return Action(
        Command.FLEE,
        mustBeFighting(),
        command(),
        listOf(Cost(CostType.MV_PERCENT, 25)),
        Command.LOOK
    ) {
        it.endFight()
        val exit = it.getExits().random()
        it.moveMob(
            exit.destination,
            exit.direction
        )
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you flee!")
                .toObservers("${it.getMob()} flees!")
                .build()
        )
    }
}
