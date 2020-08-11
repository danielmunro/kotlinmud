package kotlinmud.action.impl.item

import kotlinmud.action.helper.mustBeStanding
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.exception.CraftException
import kotlinmud.io.factory.createCraftMessage
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.recipe
import kotlinmud.io.type.Syntax
import kotlinmud.item.type.Recipe

fun createCraftAction(): Action {
    return Action(Command.CRAFT, mustBeStanding(), recipe()) { svc ->
        val recipe: Recipe = svc.get(Syntax.RECIPE)

        try {
            svc.craft(recipe)
        } catch (craftException: CraftException) {
            return@Action svc.createOkResponse(
                messageToActionCreator("you don't have all the necessary components.")
            )
        }

        svc.createOkResponse(createCraftMessage(svc.getMob(), recipe), 2)
    }
}
