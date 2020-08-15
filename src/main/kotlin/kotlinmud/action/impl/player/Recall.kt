package kotlinmud.action.impl.player

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.command
import kotlinmud.io.model.EmptyResponse
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import org.jetbrains.exposed.sql.transactions.transaction

fun createRecallAction(): Action {
    return Action(
        Command.RECALL,
        mustBeAlert(),
        command(),
        listOf(1),
        listOf(Cost(CostType.MV_PERCENT, 50)),
        Command.LOOK
    ) {
        transaction { it.getMob().room = it.getRecall() }
        EmptyResponse()
    }
}
