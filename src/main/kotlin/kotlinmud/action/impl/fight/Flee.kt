package kotlinmud.action.impl.fight

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.helper.mustBeFighting
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.model.EmptyResponse
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinx.coroutines.runBlocking

fun createFleeAction(): Action {
    return ActionBuilder(Command.FLEE).also {
        it.dispositions = mustBeFighting()
        it.costs = listOf(Cost(CostType.MV_PERCENT, 25))
        it.chainTo = Command.LOOK
    } build {
        runBlocking { it.flee() }
        EmptyResponse()
    }
}
