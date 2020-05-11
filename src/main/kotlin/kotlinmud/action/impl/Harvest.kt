package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.exception.HarvestException
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.io.itemInRoom
import kotlinmud.item.Item
import kotlinmud.item.Recipe

fun createHarvestAction(): Action {
    return Action(Command.HARVEST, mustBeStanding(), itemInRoom()) { svc ->
        val recipe = svc.get<Recipe>(Syntax.ITEM_TO_HARVEST)
        val item = svc.get<Item>(Syntax.ITEM_TO_HARVEST)
        try {
            svc.harvest(recipe)
            svc.createResponse(Message("you successfully harvest $item into ${recipe.name}."))
        } catch (exception: HarvestException) {
            return@Action svc.createResponse(Message("you can't find it anywhere."))
        }
    }
}
