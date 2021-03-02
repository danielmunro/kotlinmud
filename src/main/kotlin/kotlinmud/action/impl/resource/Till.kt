package kotlinmud.action.impl.resource

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.biome.type.SubstrateType
import kotlinmud.io.factory.command
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction

fun createTillAction(): Action {
    return Action(
        Command.TILL,
        mustBeAlert(),
        command(),
        listOf(0),
        listOf(Cost(CostType.MV_PERCENT, 1))
    ) {
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
