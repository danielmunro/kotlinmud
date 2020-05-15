package kotlinmud.world

import kotlinmud.world.resource.Brush
import kotlinmud.world.resource.Coal
import kotlinmud.world.resource.Ice
import kotlinmud.world.resource.JungleTree
import kotlinmud.world.resource.PineTree
import kotlinmud.world.resource.Resource
import kotlinmud.world.resource.Tar

fun createResourceList(): List<Resource> {
    return listOf(
        PineTree(),
        Brush(),
        Coal(),
        Ice(),
        JungleTree(),
        Tar()
    )
}
