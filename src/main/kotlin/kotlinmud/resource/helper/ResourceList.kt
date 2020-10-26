package kotlinmud.resource.helper

import kotlinmud.resource.impl.BlackberryBush
import kotlinmud.resource.impl.CoalOre
import kotlinmud.resource.impl.GoldOre
import kotlinmud.resource.impl.IronOre
import kotlinmud.resource.impl.JungleTree
import kotlinmud.resource.impl.PineTree
import kotlinmud.resource.impl.Tar
import kotlinmud.resource.impl.Watermelon
import kotlinmud.resource.impl.WildGrass
import kotlinmud.resource.type.Resource

fun createResourceList(): List<Resource> {
    return listOf(
        WildGrass(),
        CoalOre(),
        GoldOre(),
        IronOre(),
        JungleTree(),
        PineTree(),
        Tar(),
        Watermelon(),
        BlackberryBush()
    )
}
