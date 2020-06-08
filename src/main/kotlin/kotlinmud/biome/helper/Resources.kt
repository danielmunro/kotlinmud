package kotlinmud.biome.helper

import kotlinmud.world.resource.Brush
import kotlinmud.world.resource.CoalOre
import kotlinmud.world.resource.GoldOre
import kotlinmud.world.resource.IronOre
import kotlinmud.world.resource.JungleTree
import kotlinmud.world.resource.PineTree
import kotlinmud.world.resource.Resource
import kotlinmud.world.resource.Tar
import kotlinmud.world.resource.Watermelon

fun createResourceList(): List<Resource> {
    return listOf(
        Brush(),
        CoalOre(),
        GoldOre(),
        IronOre(),
        JungleTree(),
        PineTree(),
        Tar(),
        Watermelon()
    )
}
