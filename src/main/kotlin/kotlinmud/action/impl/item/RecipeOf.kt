package kotlinmud.action.impl.item

import kotlinmud.action.helper.mustBeAlive
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createRecipeOfMessage
import kotlinmud.io.factory.subcommandWithRecipe
import kotlinmud.io.type.Syntax

fun createRecipeOfAction(): Action {
    return Action(Command.RECIPE_OF, mustBeAlive(), subcommandWithRecipe()) {
        it.createOkResponse(createRecipeOfMessage(it.get(Syntax.RECIPE)))
    }
}
