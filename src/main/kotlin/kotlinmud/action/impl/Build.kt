package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.io.recipe
import kotlinmud.item.Recipe

fun createBuildAction(): Action {
    return Action(Command.BUILD, mustBeStanding(), recipe()) { svc ->
        val recipe: Recipe = svc.get(Syntax.RECIPE)
        val mob = svc.getMob()
        recipe.getProducts().forEach {
            svc.changeItemOwner(it.copy(), mob)
        }
        svc.createResponse(
            Message(
                "foobar"
            )
        )
    }
}
