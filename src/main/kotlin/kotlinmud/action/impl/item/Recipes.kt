package kotlinmud.action.impl.item

import kotlinmud.action.helper.mustBeAlive
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.command
import kotlinmud.io.factory.createRecipesMessage

fun createRecipesAction(): Action {
    return Action(Command.RECIPES, mustBeAlive(), command()) {
        it.createOkResponse(createRecipesMessage(it.recipes))
    }
}
