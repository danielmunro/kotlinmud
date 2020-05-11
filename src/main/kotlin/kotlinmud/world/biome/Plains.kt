package kotlinmud.world.biome

import kotlinmud.world.resource.Resource
import kotlinmud.world.resource.Tree

class Plains : Biome {
    override val id: Int = 2
    override val biomeType: BiomeType = BiomeType.PLAINS
    override val resources: Map<Resource, Double> = mapOf(
        Pair(Tree(), 0.05)
    )
}
