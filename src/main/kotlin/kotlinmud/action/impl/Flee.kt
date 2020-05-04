package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeFighting
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.mob.skill.Cost
import kotlinmud.mob.skill.CostType

fun createFleeAction(): Action {
    return Action(
        Command.FLEE,
        mustBeFighting(),
        listOf(Syntax.COMMAND),
        {
            it.endFightFor(it.getMob())
            val exit = it.getRoom().exits.random()
            it.moveMob(
                it.getMob(),
                exit.destination,
                exit.direction)
            it.createResponse(
                Message(
                    "you flee!",
                    "${it.getMob()} flees!")
            )
        },
        listOf(
            Cost(CostType.MV_PERCENT, 25)
        ),
        Command.LOOK
    )
}
