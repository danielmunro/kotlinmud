package kotlinmud.item.helper

import kotlinmud.item.recipe.BuilderTableRecipe
import kotlinmud.item.recipe.LumberRecipe
import kotlinmud.item.recipe.ShelterRecipe
import kotlinmud.item.recipe.SleepingBagRecipe
import kotlinmud.item.recipe.StickRecipe
import kotlinmud.item.recipe.TorchRecipe
import kotlinmud.item.type.Recipe

fun createRecipeList(): List<Recipe> {
    return listOf(
        BuilderTableRecipe(),
        LumberRecipe(),
        ShelterRecipe(),
        StickRecipe(),
        TorchRecipe(),
        SleepingBagRecipe()
    )
}
