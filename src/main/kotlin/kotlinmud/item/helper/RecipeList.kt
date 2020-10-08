package kotlinmud.item.helper

import kotlinmud.item.recipe.BuilderTableRecipe
import kotlinmud.item.recipe.GlassBlockRecipe
import kotlinmud.item.recipe.LumberRecipe
import kotlinmud.item.recipe.StickRecipe
import kotlinmud.item.recipe.TorchRecipe
import kotlinmud.item.recipe.equipment.IronAxeRecipe
import kotlinmud.item.recipe.equipment.IronPickaxeRecipe
import kotlinmud.item.recipe.equipment.IronSwordRecipe
import kotlinmud.item.recipe.equipment.StoneAxeRecipe
import kotlinmud.item.recipe.equipment.StonePickaxeRecipe
import kotlinmud.item.recipe.equipment.StoneSwordRecipe
import kotlinmud.item.recipe.equipment.WoodenAxeRecipe
import kotlinmud.item.recipe.equipment.WoodenPickaxeRecipe
import kotlinmud.item.recipe.equipment.WoodenSwordRecipe
import kotlinmud.item.recipe.shelter.BedRecipe
import kotlinmud.item.recipe.shelter.ShelterRecipe
import kotlinmud.item.recipe.shelter.SleepingBagRecipe
import kotlinmud.item.type.Recipe

fun createRecipeList(): List<Recipe> {
    return listOf(
        BuilderTableRecipe(),
        LumberRecipe(),
        StickRecipe(),
        TorchRecipe(),
        GlassBlockRecipe(),

        // shelter
        BedRecipe(),
        SleepingBagRecipe(),
        ShelterRecipe(),

        // wooden tools
        WoodenSwordRecipe(),
        WoodenAxeRecipe(),
        WoodenPickaxeRecipe(),

        // stone tools
        StoneAxeRecipe(),
        StonePickaxeRecipe(),
        StoneSwordRecipe(),

        // iron
        IronSwordRecipe(),
        IronPickaxeRecipe(),
        IronAxeRecipe()
    )
}
