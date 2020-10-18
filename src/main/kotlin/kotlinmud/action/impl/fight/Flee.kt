package kotlinmud.action.impl.fight

import kotlinmud.action.helper.mustBeFighting
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.command
import kotlinmud.io.model.EmptyResponse
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinx.coroutines.runBlocking

fun createFleeAction(): Action {
    return Action(
        Command.FLEE,
        mustBeFighting(),
        command(),
        listOf(0),
        listOf(Cost(CostType.MV_PERCENT, 25)),
        Command.LOOK
    ) {
        runBlocking { it.flee() }
        EmptyResponse()
    }
}
