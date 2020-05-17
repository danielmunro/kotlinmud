package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.exception.HarvestException
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.messageToActionCreator
import kotlinmud.io.resourceInRoom
import kotlinmud.mob.skill.Cost
import kotlinmud.mob.skill.CostType
import kotlinmud.world.ResourceType

fun createHarvestAction(): Action {
    return Action(
        Command.HARVEST,
        mustBeStanding(),
        resourceInRoom(),
        listOf(
            Cost(CostType.DELAY, 1),
            Cost(CostType.MV_PERCENT, 1)
        )) { svc ->
        val resourceType = svc.get<ResourceType>(Syntax.RESOURCE_IN_ROOM)
        try {
            svc.harvest(resourceType)
            svc.createResponse(
                MessageBuilder()
                    .toActionCreator("you successfully harvest ${resourceType.value}.")
                    .toObservers("${svc.getMob()} harvests ${resourceType.value}.")
                    .build()
            )
        } catch (exception: HarvestException) {
            svc.createResponse(messageToActionCreator("you can't find it anywhere."))
        }
    }
}
