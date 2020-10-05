package kotlinmud.item.helper

import kotlinmud.item.recipe.BuilderTableRecipe
import kotlinmud.item.recipe.LumberRecipe
import kotlinmud.item.recipe.StickRecipe
import kotlinmud.item.recipe.TorchRecipe
import kotlinmud.item.recipe.equipment.WoodenAxeRecipe
import kotlinmud.item.recipe.equipment.WoodenPickaxeRecipe
import kotlinmud.item.recipe.equipment.WoodenSwordRecipe
import kotlinmud.item.recipe.shelter.ShelterRecipe
import kotlinmud.item.recipe.shelter.SleepingBagRecipe
import kotlinmud.item.type.Recipe

fun createRecipeList(): List<Recipe> {
    return listOf(
        BuilderTableRecipe(),
        LumberRecipe(),
        ShelterRecipe(),
        StickRecipe(),
        TorchRecipe(),
        SleepingBagRecipe(),
        WoodenSwordRecipe(),
        WoodenAxeRecipe(),
        WoodenPickaxeRecipe()
    )
}
