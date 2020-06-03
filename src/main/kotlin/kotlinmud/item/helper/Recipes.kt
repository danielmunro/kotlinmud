package kotlinmud.item.helper

import kotlinmud.item.recipe.BuilderTable
import kotlinmud.item.recipe.Lumber
import kotlinmud.item.recipe.Shelter
import kotlinmud.item.type.Recipe

fun createRecipeList(): List<Recipe> {
    return listOf(
        BuilderTable(),
        Lumber(),
        Shelter()
    )
}
