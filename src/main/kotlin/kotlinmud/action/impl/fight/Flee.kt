package kotlinmud.action.impl.fight

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.helper.mustBeFighting
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.affect.type.AffectType
import kotlinmud.helper.math.dice
import kotlinmud.io.model.EmptyResponse
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinx.coroutines.runBlocking

fun createFleeAction(): Action {
    return ActionBuilder(Command.FLEE).also {
        it.dispositions = mustBeFighting()
        it.costs = listOf(Cost(CostType.MV_PERCENT, 25))
        it.chainTo = Command.LOOK
    } build {
        val mob = it.getMob()
        val isStunned = mob.affects.find { affect -> affect.type == AffectType.STUNNED } != null

        if (isStunned && dice(1, 2) == 1) {
            return@build it.createOkResponse(
                MessageBuilder()
                    .toActionCreator("You attempt to flee but stumble in a stunned stupor!")
                    .toObservers("$mob attempts to flee but stumbles in a stunned stupor!")
                    .build()
            )
        }

        runBlocking { it.flee() }
        EmptyResponse()
    }
}
