package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.item.type.Recipe

class RecipeContextBuilder(private val recipeList: List<Recipe>) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return recipeList.find {
            matches(it.name, word)
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(syntax, Status.FAILED, "that's not a recipe")
    }
}
