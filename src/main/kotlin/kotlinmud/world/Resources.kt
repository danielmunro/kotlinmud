package kotlinmud.world

import kotlinmud.world.resource.Brush
import kotlinmud.world.resource.CoalOre
import kotlinmud.world.resource.GoldOre
import kotlinmud.world.resource.IronOre
import kotlinmud.world.resource.JungleTree
import kotlinmud.world.resource.PineTree
import kotlinmud.world.resource.Resource
import kotlinmud.world.resource.Tar

fun createResourceList(): List<Resource> {
    return listOf(
        Brush(),
        CoalOre(),
        GoldOre(),
        IronOre(),
        JungleTree(),
        PineTree(),
        Tar()
    )
}
