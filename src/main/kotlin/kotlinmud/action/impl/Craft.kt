package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.exception.CraftException
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.io.recipe
import kotlinmud.item.Recipe

fun createCraftAction(): Action {
    return Action(Command.CRAFT, mustBeStanding(), recipe()) { svc ->
        val recipe: Recipe = svc.get(Syntax.RECIPE)
        val mob = svc.getMob()

        try {
            svc.craft(recipe, mob)
        } catch (craftException: CraftException) {
            return@Action svc.createResponse(Message("you don't have all the necessary components."))
        }

        svc.createResponse(
            Message(
                "you craft ${recipe.name}.",
                "$mob crafts ${recipe.name}."
            )
        )
    }
}
