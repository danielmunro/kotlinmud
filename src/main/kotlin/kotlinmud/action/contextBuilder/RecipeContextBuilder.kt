package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.Syntax
import kotlinmud.item.Recipe
import kotlinmud.string.matches

class RecipeContextBuilder(private val recipeList: List<Recipe>) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return recipeList.find {
            matches(it.name, word)
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(syntax, Status.FAILED, "that's not a recipe")
    }
}
