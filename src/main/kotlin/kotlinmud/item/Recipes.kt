package kotlinmud.item

import kotlinmud.item.recipe.BuilderTable
import kotlinmud.item.recipe.Lumber
import kotlinmud.item.recipe.Shelter

fun createRecipeList(): List<Recipe> {
    return listOf(
        BuilderTable(),
        Lumber(),
        Shelter()
    )
}
