package kotlinmud.world.biome

import kotlinmud.world.resource.Resource
import kotlinmud.world.resource.Tree

class Arboreal : Biome {
    override val id: Int = 1
    override val biomeType: BiomeType = BiomeType.ARBOREAL
    override val resources: Map<Resource, Double> = mapOf(
        Pair(Tree(), 0.75)
    )
}
