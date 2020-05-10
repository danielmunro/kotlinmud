package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.io.recipe
import kotlinmud.item.Item
import kotlinmud.item.Recipe

fun createCraftAction(): Action {
    return Action(Command.CRAFT, mustBeStanding(), recipe()) { svc ->
        val recipe: Recipe = svc.get(Syntax.RECIPE)
        val mob = svc.getMob()
        val items = svc.getItemsFor(mob)
        val toDestroy: MutableList<Item> = mutableListOf()
        recipe.getComponents().forEach { component ->
            for (i: Int in 2..component.value) {
                items.find { it.type == component.key && !toDestroy.contains(it) }?.let {
                    toDestroy.add(it)
                } ?: return@Action svc.createResponse(Message("you don't have all the necessary components."))
            }
        }
        toDestroy.forEach {
            println(it)
            svc.destroy(it)
        }
        recipe.getProducts().forEach {
            svc.changeItemOwner(it.copy(), mob)
        }
        svc.createResponse(
            Message(
                "you craft ${recipe.name}.",
                "$mob crafts ${recipe.name}."
            )
        )
    }
}
