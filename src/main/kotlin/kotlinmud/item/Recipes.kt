package kotlinmud.item

import kotlinmud.item.recipe.BuilderTable

fun createRecipeList(): List<Recipe> {
    return listOf(
        BuilderTable()
    )
}
