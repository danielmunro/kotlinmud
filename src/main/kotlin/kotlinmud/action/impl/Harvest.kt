package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.io.itemInRoom
import kotlinmud.item.Recipe

fun createHarvestAction(): Action {
    return Action(Command.HARVEST, mustBeStanding(), itemInRoom()) { svc ->
        val recipe = svc.get<Recipe>(Syntax.ITEM_TO_HARVEST)
        svc.getItemsFor(svc.getRoom()).find {
            it.type == recipe.getComponents().keys.first()
        }?.let {
            svc.destroy(it)
            recipe.getProducts().forEach { product ->
                svc.changeItemOwner(product.copy(), svc.getRoom())
            }
            svc.createResponse(Message("you successfully harvest $it into ${recipe.name}."))
        } ?: svc.createResponse(Message("you can't find it anywhere."))
    }
}
