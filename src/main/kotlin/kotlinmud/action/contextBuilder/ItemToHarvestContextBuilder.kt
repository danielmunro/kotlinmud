package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.io.Syntax
import kotlinmud.item.Item
import kotlinmud.item.Recipe
import kotlinmud.string.matches

class ItemToHarvestContextBuilder(private val itemsInRoom: List<Item>, private val recipeList: List<Recipe>) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val item = itemsInRoom.find { matches(it.name, word) }
            ?: return Context(syntax, Status.FAILED, "you don't see that anywhere.")
        return recipeList.find {
            it.getComponents().size == 1 && it.getComponents().keys.first() == item.type
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(syntax, Status.FAILED, "you can't harvest that.")
    }
}
