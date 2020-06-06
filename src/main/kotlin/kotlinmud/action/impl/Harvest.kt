package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeStanding
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.exception.HarvestException
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.resourceInRoom
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinmud.world.ResourceType

fun createHarvestAction(): Action {
    return Action(
        Command.HARVEST,
        mustBeStanding(),
        resourceInRoom(),
        listOf(
            Cost(CostType.MV_PERCENT, 1)
        )
    ) { svc ->
        val resourceType = svc.get<ResourceType>(Syntax.RESOURCE_IN_ROOM)
        try {
            svc.harvest(resourceType)
            svc.createOkResponse(
                MessageBuilder()
                    .toActionCreator("you successfully harvest ${resourceType.value}.")
                    .toObservers("${svc.getMob()} harvests ${resourceType.value}.")
                    .build(),
                2
            )
        } catch (exception: HarvestException) {
            svc.createOkResponse(messageToActionCreator("you can't find it anywhere."))
        }
    }
}
