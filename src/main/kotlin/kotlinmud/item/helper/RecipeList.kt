package kotlinmud.item.helper

import kotlinmud.item.recipe.BuilderTableRecipe
import kotlinmud.item.recipe.GlassBlockRecipe
import kotlinmud.item.recipe.LumberRecipe
import kotlinmud.item.recipe.StickRecipe
import kotlinmud.item.recipe.TorchRecipe
import kotlinmud.item.recipe.equipment.chest.StoneChestPlateRecipe
import kotlinmud.item.recipe.equipment.feet.StoneShoesRecipe
import kotlinmud.item.recipe.equipment.helmet.StoneHelmetRecipe
import kotlinmud.item.recipe.equipment.leggings.StoneLeggingsRecipe
import kotlinmud.item.recipe.equipment.wield.DiamondAxeRecipe
import kotlinmud.item.recipe.equipment.wield.DiamondPickAxeRecipe
import kotlinmud.item.recipe.equipment.wield.DiamondSwordRecipe
import kotlinmud.item.recipe.equipment.wield.IronAxeRecipe
import kotlinmud.item.recipe.equipment.wield.IronPickAxeRecipe
import kotlinmud.item.recipe.equipment.wield.IronSwordRecipe
import kotlinmud.item.recipe.equipment.wield.StoneAxeRecipe
import kotlinmud.item.recipe.equipment.wield.StonePickaxeRecipe
import kotlinmud.item.recipe.equipment.wield.StoneSwordRecipe
import kotlinmud.item.recipe.equipment.wield.WoodenAxeRecipe
import kotlinmud.item.recipe.equipment.wield.WoodenPickAxeRecipe
import kotlinmud.item.recipe.equipment.wield.WoodenSwordRecipe
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
        WoodenPickAxeRecipe(),

        // stone tools
        StoneAxeRecipe(),
        StonePickaxeRecipe(),
        StoneSwordRecipe(),

        // iron
        IronSwordRecipe(),
        IronPickAxeRecipe(),
        IronAxeRecipe(),

        // diamond
        DiamondAxeRecipe(),
        DiamondPickAxeRecipe(),
        DiamondSwordRecipe(),

        // helmet
        StoneHelmetRecipe(),

        // chest plates
        StoneChestPlateRecipe(),

        // leggings
        StoneLeggingsRecipe(),

        // feet
        StoneShoesRecipe()
    )
}
