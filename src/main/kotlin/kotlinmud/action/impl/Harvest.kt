package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.exception.HarvestException
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.itemInRoom
import kotlinmud.io.messageToActionCreator
import kotlinmud.item.Item
import kotlinmud.item.Recipe

fun createHarvestAction(): Action {
    return Action(Command.HARVEST, mustBeStanding(), itemInRoom()) { svc ->
        val recipe = svc.get<Recipe>(Syntax.ITEM_TO_HARVEST)
        val item = svc.get<Item>(Syntax.ITEM_TO_HARVEST)
        try {
            svc.harvest(recipe)
            svc.createResponse(
                MessageBuilder()
                    .toActionCreator("you successfully harvest $item into ${recipe.name}.")
                    .toObservers("${svc.getMob()} harvests $item into ${recipe.name}.")
                    .build()
            )
        } catch (exception: HarvestException) {
            svc.createResponse(messageToActionCreator("you can't find it anywhere."))
        }
    }
}
