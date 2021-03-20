package kotlinmud.action.impl.resource

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.biome.type.SubstrateType
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinx.coroutines.runBlocking

fun createTillAction(): Action {
    return ActionBuilder(Command.TILL).also {
        it.costs = listOf(Cost(CostType.MV_PERCENT, 1))
    } build {
        when (it.getRoom().substrateType) {
            SubstrateType.TILLED_DIRT, SubstrateType.DIRT -> {
                runBlocking { it.publishTillEvent(it.getRoom()) }
                it.createOkResponse(
                    MessageBuilder()
                        .toActionCreator("You till the ground.")
                        .toObservers("${it.getMob()} tills the ground.")
                        .build()
                )
            }
            else -> it.createErrorResponse(
                MessageBuilder()
                    .toActionCreator("You cannot till here.")
                    .build()
            )
        }
    }
}
