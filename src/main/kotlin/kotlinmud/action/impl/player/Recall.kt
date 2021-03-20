package kotlinmud.action.impl.player

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.command
import kotlinmud.io.model.EmptyResponse
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType

fun createRecallAction(): Action {
    return ActionBuilder(Command.RECALL).also {
        it.syntax = command()
        it.costs = listOf(Cost(CostType.MV_PERCENT, 50))
        it.chainTo = Command.LOOK
    } build {
        it.getMob().room = it.getRecall()
        EmptyResponse()
    }
}
