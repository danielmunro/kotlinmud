package kotlinmud.world.biome

import kotlinmud.world.resource.Resource
import kotlinmud.world.resource.Tree

class Arboreal : Biome {
    override val biomeType: BiomeType = BiomeType.ARBOREAL
    override val resources: Map<Resource, Double> = mapOf(
        Pair(Tree(), 0.75)
    )
}
