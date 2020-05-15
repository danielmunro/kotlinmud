package kotlinmud.world.biome

import kotlinmud.world.resource.PineTree
import kotlinmud.world.resource.Resource

class Plains : Biome {
    override val biomeType: BiomeType = BiomeType.PLAINS
    override val resources: Map<Resource, Double> = mapOf(
        Pair(PineTree(), 0.05)
    )
}
