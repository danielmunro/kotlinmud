package kotlinmud.action.impl.resource

import kotlinmud.action.builder.ActionBuilder
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
    return ActionBuilder(Command.HARVEST).also {
        it.dispositions = mustBeStanding()
        it.syntax = resourceInRoom()
        it.costs = listOf(Cost(CostType.MV_PERCENT, 1))
    } build { svc ->
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
