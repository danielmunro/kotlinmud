package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.EmptyResponse
import kotlinmud.io.Syntax
import kotlinmud.mob.skill.Cost
import kotlinmud.mob.skill.CostType

fun createRecallAction(): Action {
    return Action(
        Command.RECALL,
        mustBeAlert(),
        listOf(Syntax.COMMAND),
        listOf(
            Cost(CostType.MV_PERCENT, 50)
        ),
        Command.LOOK) {
            it.putMobInRoom(it.getRecall())
            EmptyResponse()
        }
}
