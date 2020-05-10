package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.io.recipe
import kotlinmud.item.Item
import kotlinmud.item.ItemType
import kotlinmud.item.Recipe

fun createCraftAction(): Action {
    return Action(Command.CRAFT, mustBeStanding(), recipe()) { svc ->
        val recipe: Recipe = svc.get(Syntax.RECIPE)
        val mob = svc.getMob()
        val componentsList = createListOfItemTypesFromMap(recipe.getComponents())
        val toDestroy = createListOfItemsToDestroy(svc.getItemsFor(mob).sortedBy { it.type }, componentsList)

        if (toDestroy.size < componentsList.size) {
            return@Action svc.createResponse(Message("you don't have all the necessary components."))
        }

        toDestroy.forEach { svc.destroy(it) }

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

fun createListOfItemTypesFromMap(components: Map<ItemType, Int>): List<ItemType> {
    val componentsList: MutableList<ItemType> = mutableListOf()
    components.toSortedMap().forEach {
        for (i in 1 until it.value) {
            componentsList.add(it.key)
        }
    }
    return componentsList
}

fun createListOfItemsToDestroy(items: List<Item>, componentsList: List<ItemType>): List<Item> {
    val toDestroy: MutableList<Item> = mutableListOf()
    var componentReq = 0
    items.forEach {
        if (componentReq < componentsList.size && it.type == componentsList[componentReq]) {
            componentReq++
            toDestroy.add(it)
        }
    }
    return toDestroy
}
