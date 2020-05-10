package kotlinmud.item

import kotlinmud.item.recipe.BuilderTable
import kotlinmud.item.recipe.Lumber

fun createRecipeList(): List<Recipe> {
    return listOf(
        BuilderTable(),
        Lumber()
    )
}
