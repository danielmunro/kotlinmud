package kotlinmud.action.impl.resource

import kotlinmud.action.helper.mustBeStanding
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.biome.type.ResourceType
import kotlinmud.exception.HarvestException
import kotlinmud.io.factory.createHarvestMessage
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.resourceInRoom
import kotlinmud.io.type.Syntax
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType

fun createHarvestAction(): Action {
    return Action(
        Command.HARVEST,
        mustBeStanding(),
        resourceInRoom(),
        listOf(0, 1),
        listOf(Cost(CostType.MV_PERCENT, 1))
    ) { svc ->
        val resource = svc.get<ResourceType>(Syntax.RESOURCE_IN_ROOM)
        try {
            svc.harvest(resource)
            svc.getRoom().resources.remove(resource)
            svc.createOkResponse(
                createHarvestMessage(svc.getMob(), resource),
                2
            )
        } catch (exception: HarvestException) {
            svc.createOkResponse(messageToActionCreator("you can't find it anywhere."))
        }
    }
}
